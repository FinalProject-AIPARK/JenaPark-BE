package com.aipark.jena.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class QueryDSLTest {

    @Autowired
    JPAQueryFactory query;


    @Test
    public void 기본쿼리(){
        List<Member> results = query
                .selectFrom(QMember.member)
                .fetch();

        assertThat(results.size()).isEqualTo(2);

    }
}
