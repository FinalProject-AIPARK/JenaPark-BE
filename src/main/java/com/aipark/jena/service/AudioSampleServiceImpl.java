package com.aipark.jena.service;

import com.aipark.jena.domain.AudioSampleRepository;
import com.aipark.jena.dto.RequestAudio;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseAudio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AudioSampleServiceImpl implements AudioSampleService{
    private final AudioSampleRepository audioSampleRepository;
    private final Response response;

    @Override
    public ResponseEntity<Response.Body> audioSampleList(RequestAudio.AudioSampleDto audioSampleDto) {
        List<ResponseAudio.AudioSampleDto> audioSampleDtos = audioSampleRepository.findAllBySexAndLang(audioSampleDto.getSex(), audioSampleDto.getLang())
                .stream()
                .map(ResponseAudio.AudioSampleDto::of)
                .collect(Collectors.toList());

        return response.success(audioSampleDtos,"오디오 샘플리스트를 성공적으로 반환했습니다.", HttpStatus.OK);
    }
}
