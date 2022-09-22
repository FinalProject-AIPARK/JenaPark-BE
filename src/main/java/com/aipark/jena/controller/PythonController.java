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

    @Value("${s3.access_key}")
    String accessKey;

    @Value("${s3.secret_key}")
    String secretKey;

    @Value("${s3.aws_default_region}")
    String region;

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
}
