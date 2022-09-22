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
@Table(name = "AUDIO_INFO")
@Entity
public class AudioInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "audio_id")
    private Long id;

    @Column(name = "line_number")
    private int lineNumber;

    @Column(name = "split_text")
    private String splitText;

    @Column(name = "pitch")
    private Long pitch;

    @Column(name = "speed")
    private Long speed;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "duration_silence")
    private Long durationSilence;

    @Column
    private String audioFileUrl;
}
