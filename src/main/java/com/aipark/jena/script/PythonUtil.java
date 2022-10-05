package com.aipark.jena.script;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class PythonUtil {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String createAudio(String text) {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/createAudio.py", accessKey, secretKey, region, text);
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
//                System.out.println(fileName);
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
}
