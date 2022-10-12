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

    @Column(name = "pitch")
    private Double pitch;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "volume")
    private Long volume;

    @Column
    private String audioModelName;

    @Column(name = "duration_silence")
    private Double durationSilence;

    @Column
    private String audioFileS3Path;

    @Column
    private String audioFileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public void updateSplitText(String text) {
        this.splitText = text;
    }

    public void updatePitch(Double pitch) {
        this.pitch = pitch;
    }

    public void updateSpeed(Double speed) {
        this.speed = speed;
    }

    public void updateDurationSilence(Double durationSilence) {
        this.durationSilence = durationSilence;
    }

    public void updateAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    public void updateAudioFileS3Path(String audioFileS3Path) {
        this.audioFileS3Path = audioFileS3Path;
    }
}
