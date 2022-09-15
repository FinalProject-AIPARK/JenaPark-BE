package com.aipark.jena.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "AVATAR")
public class Avatar {

    @Id
    @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String thumbNail;

    @Column
    private int attitude;

    @Column
    private int clothes;

    @Column
    private int Accessories;
}
