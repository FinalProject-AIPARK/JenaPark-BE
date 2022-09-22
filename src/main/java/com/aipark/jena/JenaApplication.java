package com.aipark.jena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JenaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JenaApplication.class, args);
    }

}
