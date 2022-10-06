package com.aipark.jena.service;

import com.aipark.jena.config.security.SecurityUtil;
import com.aipark.jena.domain.*;
import com.aipark.jena.dto.Response;
import com.aipark.jena.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.aipark.jena.dto.Response.Body;

@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService{
    private final Response response;
    private final VideoRepository videoRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public ResponseEntity<Body> createVideo(Long projectId) {
        Member member = checkToken();
        Project project = checkProject(projectId);
        checkProjectValidation(projectId, member);

        Video video = Video.builder()
                .member(member)
                .title(project.getTitle())
                .videoFileUrl("제작 중")
                .avatarUrl(project.getAvatarUrl())
                .build();
        member.addVideo(video);
        videoRepository.save(video);
        return response.success("영상이 성공적으로 생성되었습니다.");
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

    // 프로젝트가 해당 유저의 소유물인지 확인
    private void checkProjectValidation(Long projectId, Member member) {
        if (!projectRepository.existsByIdAndMember(projectId, member)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "해당 프로젝트에 접근할 수 없습니다.");
        }
    }
}
