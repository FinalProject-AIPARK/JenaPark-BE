package com.aipark.jena.domain;

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
@Table(name = "AUDIO_SAMPLE")
@Entity
public class AudioSample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audio_sample_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String sex;

    @Column
    private String lang;

    @Column
    private String audioFileUrl;  // s3에 저장된 객체 url
}
