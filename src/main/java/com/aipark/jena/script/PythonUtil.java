package com.aipark.jena.script;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class PythonUtil {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String createAudioInfo() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python3", "python/audio.py", accessKey, secretKey, region);
        Process p = pb.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String s3connectionStatus = in.readLine();
        String fileName = "";
        if (s3connectionStatus.equals("s3 bucket connected!")) {
            fileName = in.readLine();
            System.out.println(fileName);
        }else{
            throw new RuntimeException("python code 오류");
        }
        p.destroy();
        return fileName;
    }
}
