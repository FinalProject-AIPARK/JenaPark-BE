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
@Table(name = "PROJECT")
@Entity
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String text;

    @Column
    private Long speed;

    @Column
    private Long pitch;

    @Column
    private String voiceModel;

    @Column
    private Long volume;

    @Column
    private Long durationSilence;

}
