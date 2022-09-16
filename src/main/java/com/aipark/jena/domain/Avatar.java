package com.aipark.jena.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "AVATAR")
@Entity
public class Avatar {

    @Id
    @GeneratedValue
    @Column(name = "avatar_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String thumbNail;

}
