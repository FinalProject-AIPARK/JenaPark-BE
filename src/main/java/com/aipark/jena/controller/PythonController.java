package com.aipark.jena.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@RequestMapping("/api/v1/python")
@RestController
public class PythonController {

    @Value("${cloud.aws.credentials.access-key")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key")
    private String secretKey;

    @Value("${cloud.aws.region.static")
    private String region;

    @GetMapping
    public String getTest() {
        return "hello";
    }

    @PostMapping
    public String test() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("python", "app.py", accessKey, secretKey, region);
        Process p = pb.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String str = "";
        while ((str = in.readLine()) != null) {
            System.out.println(str);
        }
        p.waitFor();
        p.destroy();
        return "test";
    }

    @PostMapping("/test")
    public String test1() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("python", "python/audio.py");
        Process p = pb.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String str = "";
        while ((str = in.readLine()) != null) {
            System.out.println(str);
        }
        p.destroy();
        return "test";
    }
}
