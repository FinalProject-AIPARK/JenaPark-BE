package com.aipark.jena.script;

import com.aipark.jena.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.aipark.jena.dto.ResponseAudio.AudioInfoDto;

@Component
public class PythonUtil {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 음성 파일들 생성
    public List<AudioInfoDto> createAudios(String text) {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/createAudios.py", accessKey, secretKey, region, text);
        List<AudioInfoDto> audioInfoDtos = new ArrayList<>();
        Process process = null;
        try {
            process = pb.start();
            int exitVal = process.waitFor();  // 자식 프로세스가 종료될 때까지 기다림
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
            if (in.readLine().equals("s3 bucket connected!")) {
                String splitText = "";
                while ((splitText = in.readLine()) != null) {
                    String audioFileS3path = in.readLine();
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
        return audioInfoDtos;
    }


    public String editAudio(String text) {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/editAudio.py", accessKey, secretKey, region, text);
        return getFileName(pb);
    }

    public String mergeAudio(String text) {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/mergeAudio.py", accessKey, secretKey, region, text);
        return getFileName(pb);
    }

    private String getFileName(ProcessBuilder pb) {
        Process process = null;
        String fileName = "";
        try {
            process = pb.start();
            int exitVal = process.waitFor();  // 자식 프로세스가 종료될 때까지 기다림
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
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
        return fileName;
    }


    // 영상 파일 생성
    public String createVideo(String audioFileS3Path) {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/createVideo.py", accessKey, secretKey, region, audioFileS3Path);
        Process process = null;
        String fileName = "";
        try {
            process = pb.start();
            int exitVal = process.waitFor();  // 자식 프로세스가 종료될 때까지 기다림
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
            if (in.readLine().equals("s3 bucket connected!")) {
                fileName = in.readLine();
                System.out.println(fileName);
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST, "파이썬 코드 오류!");
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
        System.out.println("test");
        return fileName;
    }
}
