package com.aipark.jena.script;

import com.aipark.jena.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.aipark.jena.dto.ResponseAudio.AudioInfoDto;

@Slf4j
@Component
public class PythonUtil {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 음성 파일들 생성
    @Async
    public CompletableFuture<List<AudioInfoDto>> createAudios(String text) {
        log.info("createAudio");
        ProcessBuilder pb = new ProcessBuilder("python3", "python/createAudios.py", accessKey, secretKey, region, text);
        List<AudioInfoDto> audioInfoDtos = new ArrayList<>();
        Process process = null;
        try {
            process = pb.start();
            int exitVal = process.waitFor();  // 자식 프로세스가 종료될 때까지 기다림
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            System.out.println(in.readLine());
            if (in.readLine().equals("s3 bucket connected!")) {
                String splitText = "";
                while ((splitText = in.readLine()) != null) {
                    String audioFileS3path = in.readLine();
                    System.out.println(splitText);
                    AudioInfoDto audioInfoDto = AudioInfoDto.builder()
                            .splitText(splitText)
                            .audioFileUrl(audioFileS3path)
                            .build();
                    audioInfoDtos.add(audioInfoDto);
                }
            } else {
                throw new RuntimeException("python code 오류");
            }
            if (exitVal != 0) {
                System.out.println("서브 프로세스가 비정상 종료되었습니다.");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            assert process != null;
            process.destroy();
        }
        return CompletableFuture.completedFuture(audioInfoDtos);
    }

    public String editAudio(String text) throws ExecutionException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/editAudio.py", accessKey, secretKey, region, text);
        return getFileName(pb).get();
    }

    public String mergeAudio(String text) throws ExecutionException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/mergeAudio.py", accessKey, secretKey, region, text);
        return getFileName(pb).get();
    }

    @Async
    public CompletableFuture<String> getFileName(ProcessBuilder pb) {
        Process process = null;
        String fileName = "";
        try {
            process = pb.start();
            int exitVal = process.waitFor();  // 자식 프로세스가 종료될 때까지 기다림
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            if (in.readLine().equals("s3 bucket connected!")) {
                fileName = in.readLine();
            } else {
                throw new RuntimeException("python code 오류");
            }
            if (exitVal != 0) {
                System.out.println("서브 프로세스가 비정상 종료되었습니다.");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            assert process != null;
            process.destroy();
        }
        return CompletableFuture.completedFuture(fileName);
    }


    // 영상 파일 생성
    @Async
    public CompletableFuture<String> createVideo(String audioFileS3Path, String avatarFileS3Path) {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/createVideo.py", accessKey, secretKey, region, audioFileS3Path, avatarFileS3Path);
        Process process = null;
        String fileName = "";
        try {
            process = pb.start();
            int exitVal = process.waitFor();  // 자식 프로세스가 종료될 때까지 기다림
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
            if (in.readLine().equals("s3 bucket connected!")) {
                in.readLine();in.readLine();in.readLine();
                in.readLine();in.readLine();in.readLine();
                in.readLine();
                fileName = in.readLine();
                System.out.println(fileName);
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST, "S3 연결 실패!");
            }
            if (exitVal != 0) {
                System.out.println("서브 프로세스가 비정상 종료되었습니다.");
                throw new CustomException(HttpStatus.BAD_REQUEST, "서브 프로세스가 비정상 종료되었습니다.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        } finally {
            assert process != null;
            process.destroy();
        }
        return CompletableFuture.completedFuture(fileName);
    }
}
