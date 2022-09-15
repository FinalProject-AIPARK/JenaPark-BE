package com.aipark.jena.service;

import com.aipark.jena.domain.AudioInfo;
import com.aipark.jena.domain.ProjectRepository;
import com.aipark.jena.dto.RequestProject;
import com.aipark.jena.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final Response response;
    private final ProjectRepository projectRepository;


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

        return response.success(audio,"음성 합성을 성공적으로 마쳤습니다.", HttpStatus.OK);
    }
}
