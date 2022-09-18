package com.aipark.jena.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
public class AvatarServiceImplTest {

    @Autowired
    AvatarService avatarService;

    @Test
    @Sql("data.sql")
    public void 아바타생성테스트(){
        avatarService.avatarList();
    }

    @Test
    @Sql("data.sql")
    public void 아바타맞추기테스트(){
        avatarService.createAvatar(2L);
    }
}
