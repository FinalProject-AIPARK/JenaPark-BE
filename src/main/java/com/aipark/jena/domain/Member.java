package com.aipark.jena.domain;

import com.aipark.jena.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "MEMBER")
@Entity
public class Member{
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Authority authority;
}
