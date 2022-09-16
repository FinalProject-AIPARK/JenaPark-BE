package com.aipark.jena.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.websocket.Encoder;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "AUDIO_INFO")
@Entity
public class AudioInfo {

    @Id
    @GeneratedValue
    @Column(name = "audio_id")
    private Long id;

    @Column(name = "split_text")
    private String splitText;

    @Column
    private Long durationSilence;

    @Column
    private Encoder.Binary audioFile;
}
