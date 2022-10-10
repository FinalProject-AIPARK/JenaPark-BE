package com.aipark.jena.service;

import com.aipark.jena.config.security.SecurityUtil;
import com.aipark.jena.domain.*;
import com.aipark.jena.dto.Response;
import com.aipark.jena.exception.CustomException;
import com.aipark.jena.script.PythonUtil;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.aipark.jena.config.ProjectDefault.VIDEOS_MAX_SIZE;
import static com.aipark.jena.dto.RequestVideo.ChangeTitle;
import static com.aipark.jena.dto.Response.Body;

@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService{
    private final Response response;
    private final VideoRepository videoRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3 amazonS3;
    private final PythonUtil pythonUtil;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.default-path}")
    private String defaultPath;

    @Value("${cloud.aws.s3.download-path}")
    private String downloadPath;

    /**
     * 비디오 생성
     * @param projectId 프로젝트 Pk
     * @return 응답객체
     */
    @Transactional
    public ResponseEntity<Body> createVideo(Long projectId) {
        Member member = checkToken();
        Project project = checkProject(projectId);
        checkProjectValidation(projectId, member);
        List<Video> videos = member.getVideos();
        videos.sort(Comparator.comparing(BaseTimeEntity::getCreatedDate));

        String videoFileS3Path = pythonUtil.createVideo(project.getAudioFileS3Path());
        Video video = Video.builder()
                .member(member)
                .title(project.getTitle())
                .videoFileS3Path(videoFileS3Path)
                .videoFileUrl(defaultPath + videoFileS3Path)
                .downloadFileUrl(downloadPath + videoFileS3Path)
                .backgroundUrl(project.getBackgroundUrl())
                .avatarUrl(project.getAvatarUrl())
                .build();
        member.addVideo(video);
        // 비디오가 5개가 넘어가면, 생성된지 가장 오래된것 삭제
        if (videos.size() > VIDEOS_MAX_SIZE) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, videos.get(0).getVideoFileS3Path()));
            videoRepository.delete(videos.get(0));
        }
        videoRepository.save(video);
        return response.success("영상이 성공적으로 생성되었습니다.");
    }

    /**
     * 비디오 이름 수정
     * @param videoId 비디오 Pk
     * @return 응답객체
     */
    @Transactional
    public ResponseEntity<Body> renameVideo(Long videoId, ChangeTitle changeTitle) {
        Member member = checkToken();
        Video video = checkVideo(videoId);
        checkVideoValidation(videoId, member);

        video.updateTitle(changeTitle.getTitle());

        return response.success("비디오 제목이 수정되었습니다.");
    }

    /**
     * 비디오 삭제
     * @param videoId 비디오 Pk
     * @return 응답객체
     */
    @Transactional
    public ResponseEntity<Body> deleteVideo(Long videoId) {
        Member member = checkToken();
        Video video = checkVideo(videoId);
        checkVideoValidation(videoId, member);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, video.getVideoFileS3Path()));
        videoRepository.delete(video);
        return response.success("비디오가 삭제되었습니다.");
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

    private Video checkVideo(Long videoId) {
        return videoRepository.findById(videoId).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "해당 비디오를 찾을 수 없습니다.")
        );
    }

    // 프로젝트가 해당 유저의 소유물인지 확인
    private void checkProjectValidation(Long projectId, Member member) {
        if (!projectRepository.existsByIdAndMember(projectId, member)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "해당 프로젝트에 접근할 수 없습니다.");
        }
    }

    private void checkVideoValidation(Long videoId, Member member) {
        if (!videoRepository.existsByIdAndMember(videoId, member)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "해당 유저로 비디오에 접근할 수 없습니다.");
        }
    }
}
