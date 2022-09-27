package com.aipark.jena.service;

import com.aipark.jena.domain.Background;
import com.aipark.jena.domain.BackgroundRepository;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseBackground;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BackgroundServiceImpl implements BackgroundService {

    private final BackgroundRepository backgroundRepository;
    private final Response response;

    @Override
    public ResponseEntity<Response.Body> backgroundUpload() {
        return null;
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
