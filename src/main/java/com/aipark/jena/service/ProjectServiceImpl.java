package com.aipark.jena.service;

import com.aipark.jena.config.security.SecurityUtil;
import com.aipark.jena.domain.*;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseAudio.AudioStage1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.aipark.jena.dto.RequestProject.CreateTTS;
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
    public ResponseEntity<Response.Body> createProject() {
        Optional<Member> res = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail());
        if (res.isEmpty()) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        Member member = res.get();
        //프로젝트 생성
        Project project = Project.builder()
                .member(member)
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
    public ResponseEntity<Response.Body> createTTS(CreateTTS ttsInputDto) {
        if (memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail()).isEmpty()) {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        if (!projectRepository.existsById(ttsInputDto.getProjectID())) {
            return response.fail("해당 프로젝트가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //오디오 객체 생성
        List<AudioInfo> audioInfos = new ArrayList<>();
        // 1. text 한문장씩 분리
        List<String> splitTexts = Arrays.stream(ttsInputDto.getText()
                        .split("\\."))
                .map(String::trim)  // 맨앞,맨뒤 공백 제거
                .filter(splitText -> !splitText.isEmpty())  // 빈 문자열 제거
                .collect(Collectors.toList());

        // 2. 문장마다 오디오파일 생성
        for (int i = 0; i < splitTexts.size(); i++) {
            //python script
            //audioFileUrl 을 하나씩 만들어야함
            //보류
            AudioInfo audioInfo = AudioInfo.builder()
                    .lineNumber(i + 1)
                    .splitText(splitTexts.get(i) + ".")
                    .durationSilence(ttsInputDto.getDurationSilence())
                    .pitch(ttsInputDto.getPitch())
                    .speed(ttsInputDto.getSpeed())
                    .volume(ttsInputDto.getVolume())
                    .audioFileUrl(null)
                    .build();
            audioInfos.add(audioInfo);
        }
        audioInfoRepository.saveAll(audioInfos);
        List<AudioInfoDto> audioInfoDtos = audioInfos.stream()
                .map(AudioInfoDto::of)
                .collect(Collectors.toList());

        //전체 텍스트 생성
        StringBuilder allText = new StringBuilder();
        for (AudioInfoDto audioInfoDto : audioInfoDtos) {
            allText.append(audioInfoDto.getSplitText()).append(" ");
        }

        AudioStage1 audioStage1 = AudioStage1.builder()
                .audioInfoDtos(audioInfoDtos)
                .text(allText.toString())
                .build();

        return response.success(audioStage1, "음성 합성을 성공적으로 마쳤습니다.", HttpStatus.OK);
    }
}
