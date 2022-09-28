package com.aipark.jena.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.DoubleStream;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String memberEmail);

    boolean existsByEmail(String email);

    Optional<Member> findByOauthId(String oauthId);
}
