package com.aipark.jena.service;

import com.aipark.jena.config.security.SecurityUtil;
import com.aipark.jena.domain.*;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.Response.Body;
import com.aipark.jena.dto.ResponseAudio.AudioStage1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.aipark.jena.config.ProjectDefault.*;
import static com.aipark.jena.dto.RequestAudio.AudioUploadDto;
import static com.aipark.jena.dto.RequestProject.*;
import static com.aipark.jena.dto.ResponseAudio.AudioInfoDto;
import static com.aipark.jena.dto.ResponseProject.InitialProject;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final Response response;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final AudioInfoRepository audioInfoRepository;

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
        if (member == null) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        if (!projectRepository.existsById(ttsInputDto.getProjectID())) {
            return response.fail("해당 프로젝트가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        if (!projectRepository.existsByIdAndMember(ttsInputDto.getProjectID(), member)) {
            return response.fail("다른 회원의 프로젝트에 접근할 수 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        Project project = projectRepository.findById(ttsInputDto.getProjectID()).orElse(null);
        // 0. 기존에 있던 프로젝트의 오디오파일들을 삭제
        audioInfoRepository.deleteAllByProject(project);
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
            //python script
            //audioFileUrl 을 하나씩 만들어야함
            //보류
            audioInfos.add(AudioInfo.builder()
                    .lineNumber(i + 1)
                    .project(project)
                    .splitText(splitTexts.get(i) + ".")
                    .durationSilence(ttsInputDto.getDurationSilence())
                    .pitch(ttsInputDto.getPitch())
                    .speed(ttsInputDto.getSpeed())
                    .volume(ttsInputDto.getVolume())
                    .audioFileUrl(null)
                    .build());
        }
        assert project != null;
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
        if (member == null) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        if (!projectRepository.existsById(ttsInputDto.getProjectID())) {
            return response.fail("해당 프로젝트가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Project project = projectRepository.findById(ttsInputDto.getProjectID()).get();
        if (!audioInfoRepository.existsByIdAndProject(ttsInputDto.getAudioID(), project)) {
            return response.fail("해당 프로젝트의 오디오파일이 아닙니다.", HttpStatus.UNAUTHORIZED);
        }
        audioInfoRepository.findById(ttsInputDto.getAudioID());

        return response.success();
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
    public ResponseEntity<Body> uploadAudio(AudioUploadDto audioUploadDto) {
        Member member = checkToken();
        if (checkToken() == null) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        if (!projectRepository.existsById(audioUploadDto.getProjectID())) {
            return response.fail("해당 프로젝트가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Project project = projectRepository.findById(audioUploadDto.getProjectID()).orElse(null);
        assert project != null;

        String audioFileBase64 = audioUploadDto.getAudioFile();

        if (audioFileBase64 == null || audioFileBase64.equals("")) {
            return response.fail("파일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        } else if (audioFileBase64.length() > 400000) {
            return response.fail("파일이 너무 큽니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            String fileName = UUID.randomUUID().toString();
            File file = new File(FileSystemView.getFileSystemView().getHomeDirectory() + "/" + fileName + ".wav");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodeBytes = decoder.decode(audioFileBase64.getBytes());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(decodeBytes);
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        // 음성이 업로드 되면 audioInfos 를 비워야 한다.
        audioInfoRepository.deleteAllByProject(project);
        project.updateAudioUpload(true);
        project.updateAudioMerge(true);
        project.updateAudioFileUrl("test.url");
        return response.success("음성 업로드를 성공했습니다.");
    }

    /**
     * 음성 합성 요청
     *
     * @param projectId 프로젝트 pk key
     * @return 응답 객체
     */
    @Transactional
    public ResponseEntity<Body> mergeAudio(Long projectId) {
        if (checkToken() == null) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        if (!projectRepository.existsById(projectId)) {
            return response.fail("해당 프로젝트가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Project project = projectRepository.findById(projectId).get();
        //* 
        // 음성 합성
        // */
        String audioFileUrl = "합성된 전체 음성.url";
        project.updateAudioMerge(true);
        project.updateAudioFileUrl(audioFileUrl);
        return response.success(audioFileUrl, "음성 합성을 성공했습니다.", HttpStatus.CREATED);
    }

    // 토큰에 해당하는 유저가 있는 지 체크
    private Member checkToken() {
        return memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail()).orElse(null);
    }
}
