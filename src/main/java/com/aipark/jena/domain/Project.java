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
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String lang;

    @Column
    private String sex;

    @Column
    private String text;

    @Column
    private String speed;

    @Column
    private String pitch;

    @Column
    private Long volume;

    @Column
    private String durationSilence;

    @Column
    private String backgroundUrl;

    @Column
    private Boolean audioUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateAudioUpload(Boolean audioUpload) {
        this.audioUpload = audioUpload;
    }

    public void updateStep1(String allText, String sex, String lang, Double durationSilence, Long volume, Double pitch, Double speed) {
        this.text = allText;
        this.sex = sex;
        this.lang = lang;
        this.durationSilence = Double.toString(durationSilence);
        this.volume = volume;
        this.pitch = Double.toString(pitch);
        this.speed = Double.toString(speed);
    }
}
