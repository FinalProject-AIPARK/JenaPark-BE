package com.aipark.jena.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // 기본적으로 실행을 대기하고 있는 Thread 갯수
        executor.setMaxPoolSize(10);     // 동시 동작하는 최대 Thread 갯수
        executor.setQueueCapacity(50);  // 최대치를 초과하는 경우 Queue에 저장하고, 여유가 생기면 하나씩 꺼냄
        executor.setBeanName("asyncTestExecutor");
        executor.initialize();
        return executor;
    }
}
