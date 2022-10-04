package com.aipark.jena.service;

import com.aipark.jena.config.security.SecurityUtil;
import com.aipark.jena.domain.*;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.Response.Body;
import com.aipark.jena.dto.ResponseAudio.AudioStage1;
import com.aipark.jena.exception.CustomException;
import com.aipark.jena.script.PythonUtil;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.aipark.jena.config.ProjectDefault.*;
import static com.aipark.jena.dto.RequestAudio.AudioUploadDto;
import static com.aipark.jena.dto.RequestProject.*;
import static com.aipark.jena.dto.ResponseAudio.AudioInfoDto;
import static com.aipark.jena.dto.ResponseProject.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final Response response;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final AudioInfoRepository audioInfoRepository;
    private final AmazonS3 amazonS3;
    private final PythonUtil pythonUtil;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.default-path}")
    private String defaultPath;

    /**
     * 프로젝트 정보 조회
     *
     * @param projectId 조회할 프로젝트 id
     * @return 응답 객체
     */
    @Transactional
    public ResponseEntity<Body> inquiryProject(Long projectId) {
        if (checkToken() == null) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        Optional<Project> projectRes = projectRepository.findById(projectId);
        if (projectRes.isEmpty()) {
            return response.fail("존재하지 않는 프로젝트입니다.", HttpStatus.BAD_REQUEST);
        }
        return response.success(InitialProject.of(projectRes.get()), "프로젝트 조회를 성공했습니다.", HttpStatus.OK);
    }

    /**
     * 현재 로그인한 유저를 주인으로
     * 프로젝트 생성
     *
     * @return 응답 객체
     */
    @Transactional
    public ResponseEntity<Body> createProject() {
        Optional<Member> res = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail());
        if (res.isEmpty()) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        Member member = res.get();
        //프로젝트 생성
        Project project = Project.builder()
                .member(member)
                .title(TITLE_DEFAULT)
                .sex(SEX_DEFAULT)
                .lang(LANGUAGE_DEFAULT)
                .speed(SPEED_DEFAULT)
                .pitch(PITCH_DEFAULT)
                .volume(VOLUME_DEFAULT)
                .durationSilence(DURATION_SILENCE_DEFAULT)
                .backgroundUrl(BACKGROUND_DEFAULT)
                .audioUpload(AUDIO_UPLOAD_DEFAULT)
                .audioMerge(AUDIO_MERGE_DEFAULT)
                .build();
        //양방향 연결 member <-> project
        member.addProject(project);
        projectRepository.save(project);
        return response.success(InitialProject.of(project), "프로젝트가 성공적으로 생성되었습니다.", HttpStatus.CREATED);
    }

    /**
     * 프로젝트 제목 변경
     *
     * @param titleInputDto 프로젝트 제목 변경 요청
     * @return 응답객체
     */
    @Transactional
    public ResponseEntity<Body> changeTitle(ChangeTitle titleInputDto) {
        if (memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail()).isEmpty()) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        if (!projectRepository.existsById(titleInputDto.getProjectID())) {
            return response.fail("해당 프로젝트가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        projectRepository.findById(titleInputDto.getProjectID())
                .ifPresent(project -> project.updateTitle(titleInputDto.getTitle()));
        return response.success("해당 프로젝트의 제목이 변경되었습니다.");
    }

    /**
     * <p>음성 합성 실행 - 텍스트 입력 페이지 </p> <br>
     * <p>
     * 전체 텍스트를 한 문장씩 분리해서 오디오파일을 생성한다.
     *
     * @param ttsInputDto 음성합성 요청 : RequestProject.CreateTTS
     * @return 응답 객체
     */
    @Transactional
    public ResponseEntity<Body> createTTS(CreateTTS ttsInputDto) {
        Member member = checkToken();
        Project project = checkProject(ttsInputDto.getProjectID());
        checkProjectValidation(project.getId(), member);
        // 0. 기존에 있던 프로젝트의 오디오파일들을 삭제
        deleteAudioInfos(project.getAudioInfos());

        // 기존에 생성된 업로드 음성이나 전체듣기 음성을 삭제한다.
        if (project.getAudioUpload() || project.getAudioMerge()) {
            deleteAudio(project.getAudioFileS3Path());
            project.updateAudioOriginName(null);
            project.updateAudioFileS3Path(null);
            project.updateAudioUpload(false);
            project.updateAudioMerge(false);
            project.updateAudioFileUrl(null);
        }
        // 1. text 한문장씩 분리
        List<String> splitTexts = Arrays.stream(ttsInputDto.getText()
                        .split("\\."))
                .map(String::trim)  // 맨앞,맨뒤 공백 제거
                .filter(splitText -> !splitText.isEmpty())  // 빈 문자열 제거
                .collect(Collectors.toList());

        //오디오 객체 생성
        List<AudioInfo> audioInfos = new ArrayList<>();
        // 2. 문장마다 오디오파일 생성
        for (int i = 0; i < splitTexts.size(); i++) {
            String audioFileS3Path = pythonUtil.createAudio(splitTexts.get(i));
            audioInfos.add(AudioInfo.builder()
                    .lineNumber(i + 1)
                    .project(project)
                    .splitText(splitTexts.get(i) + ".")
                    .durationSilence(ttsInputDto.getDurationSilence())
                    .pitch(ttsInputDto.getPitch())
                    .speed(ttsInputDto.getSpeed())
                    .volume(ttsInputDto.getVolume())
                    .audioFileS3Path(audioFileS3Path)
                    .audioFileUrl(defaultPath + audioFileS3Path)
                    .build());
        }
        project.updateAudioInfos(audioInfos);
        audioInfoRepository.saveAll(audioInfos);
        List<AudioInfoDto> audioInfoDtos = audioInfos.stream()
                .map(AudioInfoDto::of)
                .collect(Collectors.toList());

        // 3. 전체 텍스트 생성
        StringBuilder allText = new StringBuilder();
        for (AudioInfoDto audioInfoDto : audioInfoDtos) {
            allText.append(audioInfoDto.getSplitText()).append(" ");
        }

        // 4 .프로젝트 업데이트
        updateProject(project, ttsInputDto, allText.toString());
        return response.success(new AudioStage1(audioInfoDtos, allText.toString()), "음성 합성을 성공적으로 마쳤습니다.", HttpStatus.OK);
    }

    /**
     * 한 오디오 파일 씩 수정 후 음성 반환
     *
     * @param ttsInputDto 선택된 음성 수정
     * @return 응답객체
     */
    @Transactional
    public ResponseEntity<Body> updateTTS(UpdateTTS ttsInputDto) {
        Member member = checkToken();
        Project project = checkProject(ttsInputDto.getProjectID());
        checkProjectValidation(ttsInputDto.getProjectID(), member);
        AudioInfo audioInfo = checkAudioInfo(ttsInputDto.getAudioID());
        if (!audioInfoRepository.existsByIdAndProject(audioInfo.getId(), project)) {
            return response.fail("해당 프로젝트의 오디오파일이 아닙니다.", HttpStatus.UNAUTHORIZED);
        }
        audioInfo.updateSplitText(ttsInputDto.getText());
        audioInfo.updatePitch(ttsInputDto.getPitch());
        audioInfo.updateSpeed(ttsInputDto.getSpeed());
        audioInfo.updateDurationSilence(ttsInputDto.getDurationSilence());

        deleteAudio(audioInfo.getAudioFileS3Path());
        // audioFileS3Path 경로 받기
        String audioFileS3Path = pythonUtil.createAudio(audioInfo.getSplitText());
        audioInfo.updateAudioFileS3Path(audioFileS3Path);
        // audioFileUrl 생성
        String audioFileUrl = defaultPath + audioFileS3Path;
        audioInfo.updateAudioFileUrl(audioFileUrl);
        audioInfoRepository.save(audioInfo);

        // 전체 텍스트 최신화
        StringBuilder allText = new StringBuilder();
        project.getAudioInfos()
                .forEach(audioInfo1 -> allText.append(audioInfo1.getSplitText()).append(" "));
        project.updateText(allText.toString());
        return response.success(UpdateTTSProject.of(audioInfo.getId(), allText.toString(), audioFileUrl), "텍스트 수정이 완료되었습니다.", HttpStatus.OK);
    }

    /**
     * 하나의 음성파일을 삭제
     *
     * @param projectId 해당 프로젝트 id
     * @param audioId   삭제하기 위한 audioId
     * @return 응답객체
     */
    @Transactional
    public ResponseEntity<Body> deleteAudioInfo(Long projectId, Long audioId) {
        Member member = checkToken();
        Project project = checkProject(projectId);
        checkProjectValidation(projectId, member);

        List<AudioInfo> audioInfos = project.getAudioInfos();
        for (AudioInfo audioInfo : audioInfos) {
            if (audioInfo.getId() == audioId) {
                deleteAudio(audioInfo.getAudioFileS3Path());
                audioInfoRepository.delete(audioInfo);
            }
        }
        return response.success("음성 파일이 삭제되었습니다.");
    }

    @Transactional
    protected void updateProject(Project project, CreateTTS ttsInputDto, String allText) {
        project.updateStep1(allText, ttsInputDto.getSex(), ttsInputDto.getLang(), ttsInputDto.getDurationSilence(),
                ttsInputDto.getVolume(), ttsInputDto.getPitch(), ttsInputDto.getSpeed());
    }

    /**
     * 오디오 업로드
     *
     * @param audioUploadDto 오디오 업로드 요청
     * @return 응답 객체
     */
    @Transactional
    public ResponseEntity<Body> uploadAudio(Long projectId, AudioUploadDto audioUploadDto) throws IOException {
        Member member = checkToken();
        Project project = checkProject(projectId);
        checkProjectValidation(projectId, member);

        InputStream inputStream = audioUploadDto.getAudioFile().getInputStream();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        String fileName = "audio/upload/" + UUID.randomUUID() + ".wav";

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
        inputStream.close();
        // 기존에 업로드된 음성을 삭제한다.
        if (project.getAudioUpload()) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, project.getAudioFileS3Path()));
        }
        // 음성이 업로드 되면 audioInfos 를 비워야 한다.
        deleteAudioInfos(project.getAudioInfos());
        String audioFileUrl = defaultPath + fileName;
        project.updateAudioUploadSuccess(audioUploadDto.getAudioFile().getOriginalFilename(), fileName, audioFileUrl);
        return response.success(audioFileUrl, "음성 업로드를 성공했습니다.", HttpStatus.CREATED);
    }

    /**
     * 업로드된 오디오 삭제
     *
     * @param projectId 해당 프로젝트 PK
     * @return 응답 객체
     */
    @Transactional
    public ResponseEntity<Body> deleteUploadAudio(Long projectId) {
        Member member = checkToken();
        Project project = checkProject(projectId);
        checkProjectValidation(projectId, member);
        if (!project.getAudioUpload()) {
            return response.fail("해당 프로젝트에 업로드된 음성파일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        deleteAudio(project.getAudioFileS3Path());
        project.updateAudioUpload(false);
        project.updateAudioMerge(false);
        project.updateAudioFileUrl(null);
        project.updateAudioFileS3Path(null);
        project.updateAudioOriginName(null);
        return response.success("업로드된 음성파일이 삭제되었습니다.");
    }


    /**
     * 로그인된 회원의 프로젝트 리스트를 리턴
     *
     * @return 응답 객체
     */
    @Transactional(readOnly = true)
    public ResponseEntity<Body> historyProject() {
        Member member = checkToken();
        if (member == null) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        List<HistoryProject> historyProjects = member.getProjects()
                .stream()
                .map(HistoryProject::of)
                .collect(Collectors.toList());
        return response.success(historyProjects, "프로젝트 히스토리를 조회합니다.", HttpStatus.OK);
    }

    /**
     * 음성 합성 요청
     *
     * @param projectId 프로젝트 pk key
     * @return 응답 객체
     */
    @Transactional
    public ResponseEntity<Body> mergeAudio(Long projectId) {
        checkToken();
        Project project = checkProject(projectId);
        // 음성파일 합성 후 오디오 파일 생성
        String audioFile = pythonUtil.createAudio(project.getText());
        String audioFileUrl = defaultPath + audioFile;
        if (project.getAudioMerge()) {
            deleteAudio(project.getAudioFileS3Path());
        }
        project.updateAudioMerge(true);
        project.updateAudioFileS3Path(audioFile);
        project.updateAudioFileUrl(audioFileUrl);
        return response.success(audioFileUrl, "음성 합성을 성공했습니다.", HttpStatus.CREATED);
    }

    /**
     * 프로젝트 삭제
     *
     * @param projectId 프로젝트 Pk
     * @return 응답객체
     */
    @Transactional
    public ResponseEntity<Body> deleteProject(Long projectId) {
        Member member = checkToken();
        Project project = checkProject(projectId);
        checkProjectValidation(projectId, member);

        // 1. audioInfos 존재한다면, 삭제
        deleteAudioInfos(project.getAudioInfos());

        // 2. 전체 미리듣기 음성파일이 존재한다면, 삭제
        if (project.getAudioFileS3Path() != null) {
            deleteAudio(project.getAudioFileS3Path());
        }

        // 3. project Entity 삭제
        projectRepository.delete(project);
        return response.success("프로젝트 삭제를 성공하였습니다.");
    }

    // 오디오 info 삭제
    @Transactional
    protected void deleteAudioInfos(List<AudioInfo> audioInfos) {
        for (AudioInfo audioInfo : audioInfos) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, audioInfo.getAudioFileS3Path()));
        }
        audioInfoRepository.deleteAll(audioInfos);
    }

    // 합성 된 오디오 전체 듣기 삭제
    protected void deleteAudio(String audioFile) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, audioFile));
    }

    // 토큰에 해당하는 유저가 있는 지 확인
    private Member checkToken() {
        return memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail()).orElseThrow(
                () -> new CustomException(HttpStatus.UNAUTHORIZED, "해당 회원을 찾을 수 없습니다.")
        );
    }

    // 프로젝트가 존재하는 지 확인
    private Project checkProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "해당 프로젝트를 찾을 수 없습니다.")
        );
    }

    // 오디오파일이 존재하는 지 확인
    private AudioInfo checkAudioInfo(Long audioInfoId) {
        return audioInfoRepository.findById(audioInfoId).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "해당 오디오 파일이 존재하지 않습니다.")
        );
    }

    // 프로젝트가 해당 유저의 소유물인지 확인
    private void checkProjectValidation(Long projectId, Member member) {
        if (!projectRepository.existsByIdAndMember(projectId, member)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "해당 프로젝트에 접근할 수 없습니다.");
        }
    }
}
