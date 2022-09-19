package com.aipark.jena.service;

import com.aipark.jena.config.security.SecurityUtil;
import com.aipark.jena.domain.*;
import com.aipark.jena.dto.RequestProject;
import com.aipark.jena.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.aipark.jena.dto.ResponseProject.InitialProject;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final Response response;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    /**
     * 현재 로그인한 유저를 주인으로
     * 프로젝트 생성
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
        member.addProject(project);
        projectRepository.save(project);
        return response.success(InitialProject.of(project), "프로젝트가 성공적으로 생성되었습니다.", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<Response.Body> createTTS(RequestProject.CreateTTS ttsInputDto) {
        if (!projectRepository.existsById(ttsInputDto.getProjectID())) {
            return response.fail("해당 프로젝트가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //오디오 객체 생성
        List<AudioInfo> audio = new ArrayList<>();
        // 1. text 한문장씩 분리
        String[] splitTexts = ttsInputDto.getText().split("\\.");
        for (String splitText : splitTexts) {
            //audioFile 을 하나씩 만들어야함
            //보류
//            ttsInputDto.getLanguage();
//            ttsInputDto.getPitch();
//            ttsInputDto.getSex();
//            ttsInputDto.getSpeed();
//            ttsInputDto.getDurationSilence();

            AudioInfo audioInfo = AudioInfo.builder()
                    .splitText(splitText)
                    .audioFile(null)
                    .build();
            audio.add(audioInfo);
        }

        return response.success(audio, "음성 합성을 성공적으로 마쳤습니다.", HttpStatus.OK);
    }
}
