package com.aipark.jena.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QueryDSLTest {

    @Autowired
    JPAQueryFactory query;
}
