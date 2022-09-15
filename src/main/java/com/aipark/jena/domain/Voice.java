package com.aipark.jena.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "VOICE")
public class Voice {

    @Id
    private Long voiceId;
    @Column
    private String voiceModel;
    @Column
    private Long speed;
    @Column
    private Long pitch;
    @Column
    private Long volume;
    @Column
    private Long durationSilence;
}
