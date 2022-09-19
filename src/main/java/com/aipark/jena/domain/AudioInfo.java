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
    @GeneratedValue
    @Column(name = "audio_id")
    private Long id;

    @Column(name = "split_text")
    private String splitText;

    @Column(name = "duration_silence")
    private Long durationSilence;

    @Lob
    @Column(name = "audio_file")
    private byte[] audioFile;
}
