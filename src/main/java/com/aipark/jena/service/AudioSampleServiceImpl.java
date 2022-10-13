package com.aipark.jena.service;

import com.aipark.jena.domain.AudioSampleRepository;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseAudio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.aipark.jena.dto.RequestAudio.AudioSampleDto;
import static com.aipark.jena.dto.Response.Body;

@RequiredArgsConstructor
@Service
public class AudioSampleServiceImpl implements AudioSampleService{
    private final AudioSampleRepository audioSampleRepository;
    private final Response response;

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<ResponseEntity<Body>> audioSampleList(AudioSampleDto audioSampleDto) {
        List<ResponseAudio.AudioSampleDto> audioSampleDtos = audioSampleRepository.findAllBySexAndLang(audioSampleDto.getSex(), audioSampleDto.getLang())
                .stream()
                .map(ResponseAudio.AudioSampleDto::of)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(response.success(audioSampleDtos, "오디오 샘플리스트를 성공적으로 반환했습니다.", HttpStatus.OK));
    }
}
