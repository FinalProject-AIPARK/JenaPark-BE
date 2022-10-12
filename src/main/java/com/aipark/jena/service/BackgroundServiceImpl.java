package com.aipark.jena.service;

import com.aipark.jena.config.security.SecurityUtil;
import com.aipark.jena.domain.*;
import com.aipark.jena.dto.RequestBackground;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseBackground;
import com.amazonaws.services.s3.AmazonS3;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BackgroundServiceImpl implements BackgroundService {

    private final BackgroundRepository backgroundRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final Response response;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    //배경 업로드
    @Override
    public ResponseEntity<Response.Body> backgroundUpload(Long projectId,RequestBackground.BackgroundUploadDto backgroundUploadDto)throws IOException {

        Project project = projectRepository.findById(projectId).orElse(null);
        assert project != null;

        InputStream inputStream = backgroundUploadDto.getBackgroundFile().getInputStream();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        String fileName = "background/" + UUID.randomUUID().toString().toLowerCase() + ".png";

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
        inputStream.close();

        String backgroundFileUrl = "https://jenapark.s3.ap-northeast-2.amazonaws.com/" + fileName;

        // bgName
        String bgName = fileName;
        //member
        Long memberId = project.getMember().getId();
        Member member = memberRepository.findById(memberId).orElseThrow();

        Background background = Background.builder()
                .bgName(bgName)
                .isUpload(true)
                .bgUrl(backgroundFileUrl)
                .member(member)
                .build();

        backgroundRepository.save(background);

        return response.success(backgroundFileUrl,"배경 업로드를 성공했습니다.",HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<Response.Body> backgroundSelect(Long projectId,Long bgId) {

        Member memberRes = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail()).orElseThrow();

        Project project = projectRepository.findById(projectId).orElseThrow();
        Background background = backgroundRepository.findById(bgId).orElseThrow();
        ResponseBackground responseBackground = new ResponseBackground(background.getId(),background.getBgName(),background.getBgUrl());

        // 유효성 검증
        if(background.isUpload() == true){
            if(backgroundRepository.findAllByMember(memberRes).isEmpty()){
                return response.fail("해당 배경아이디는 존재 하지 않습니다.",HttpStatus.BAD_REQUEST);
            }
        }

        project.updateBackgroundUrl(responseBackground.getBgUrl());
        projectRepository.save(project);

        return response.success(responseBackground.getBgUrl(),responseBackground.getBgName()+" 배경을 선택했습니다.",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response.Body> backgroundList() {
        Optional<Member> memberRes = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail());
        if(memberRes.isPresent()){
            List<Background> backgroundListDefault = backgroundRepository.findAllByIsUpload(false);
            List<Background> backgroundListMember = backgroundRepository.findAllByMember(memberRes.get());
            return response.success(responseBackgroundList(backgroundListDefault,backgroundListMember),"배경화면 리스트입니다.", HttpStatus.OK);
        }
        return response.fail("해당 회원은 존재하지 않습니다.",HttpStatus.UNAUTHORIZED);

    }

    public ResponseBackground.BackgroundAll responseBackgroundList(List<Background> b1, List<Background> b2){

        List<ResponseBackground.BackgroundDefault> responseBackgroundListDefault = new ArrayList<>();
        List<ResponseBackground.BackgroundUpload> responseBackgroundListMember = new ArrayList<>();

        for (int index = 0; index < b1.size(); index++) {
            Background background = b1.get(index);
            ResponseBackground.BackgroundDefault responseBackground = new ResponseBackground.BackgroundDefault(
                    background.getId(),
                    background.getBgName(),
                    background.getBgUrl()
            );
            responseBackgroundListDefault.add(responseBackground);
        }

        for (int index = 0; index < b2.size(); index++) {
            Background background = b2.get(index);
            ResponseBackground.BackgroundUpload responseBackground = new ResponseBackground.BackgroundUpload(
                    background.getId(),
                    background.getBgName(),
                    background.getBgUrl()
            );
            responseBackgroundListMember.add(responseBackground);
        }

        ResponseBackground.BackgroundAll responseBackground = new ResponseBackground.BackgroundAll(responseBackgroundListDefault,responseBackgroundListMember);
        return responseBackground;
    }
}
