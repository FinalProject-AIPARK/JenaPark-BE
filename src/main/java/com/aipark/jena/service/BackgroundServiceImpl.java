package com.aipark.jena.service;

import com.aipark.jena.domain.Background;
import com.aipark.jena.domain.BackgroundRepository;
import com.aipark.jena.dto.RequestBackground;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseBackground;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BackgroundServiceImpl implements BackgroundService {

    private final BackgroundRepository backgroundRepository;
    private final Response response;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    //배경 업로드
    @Override
    public ResponseEntity<Response.Body> backgroundUpload(RequestBackground.BackgroundUploadDto backgroundUploadDto)throws IOException {

        InputStream inputStream = backgroundUploadDto.getBackgroundFile().getInputStream();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        String fileName = "background/" + UUID.randomUUID().toString().toLowerCase() + ".png";

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));

        return response.success("배경 업로드를 성공했습니다.");
    }

    @Override
    public ResponseEntity<Response.Body> backgroundSelect(Long bgId) {
        Background background = backgroundRepository.findById(bgId).orElseThrow();
        ResponseBackground responseBackground = new ResponseBackground(background.getId(),background.getBgName(),background.getBgUrl());

        return response.success(responseBackground.getBgUrl(),responseBackground.getBgName()+" 배경을 선택했습니다.",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response.Body> backgroundList() {
        List<Background> backgroundList = backgroundRepository.findAll();
        List<ResponseBackground> responseBackgroundList = new ArrayList<>();


        for (int index = 0; index < backgroundList.size(); index++) {
            Background background = backgroundList.get(index);
            ResponseBackground responseBackground = new ResponseBackground(
                    background.getId(),
                    background.getBgName(),
                    background.getBgUrl()
            );
            responseBackgroundList.add(responseBackground);
        }

        return response.success(responseBackgroundList,"배경화면 리스트입니다.", HttpStatus.OK);
    }
}
