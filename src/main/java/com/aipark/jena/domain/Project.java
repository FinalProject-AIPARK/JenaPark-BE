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
    private Double speed;

    @Column
    private Double pitch;

    @Column
    private Long volume;

    @Column
    private Double durationSilence;

    @Column
    private String backgroundUrl;

    @Column
    private String audioModel;

    @Column
    private String audioModelUrl;

    @Column
    private Boolean audioUpload;    // 오디오 업로드 여부

    @Column
    private Boolean audioMerge;     // 오디오 합성여부 미리듣기나 다운로드를 하려면 true

    @Column
    private String audioFileS3Path;

    @Column
    private String audioFileOriginName;

    @Column
    private String audioFileUrl;   // 전체 오디오 파일 url 미리듣기 등

    @Column
    private String downloadAudioUrl; // 다운로드용 url

    @Column
    private String avatarUrl;   // 아바타 썸네일 이미지

    @Builder.Default
    @Column
    private Boolean checkText = false;

    @Builder.Default
    @Column
    private Boolean checkAudio = false;

    @Builder.Default
    @Column
    private Boolean checkAvatar = false;

    @Builder.Default
    @OneToMany(mappedBy = "project")
    private List<AudioInfo> audioInfos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void updateAudioUpload(Boolean audioUpload) {
        this.audioUpload = audioUpload;
    }

    public void updateAudioMerge(Boolean audioMerge) {
        this.checkText = true;
        this.checkAudio = true;
        this.audioMerge = audioMerge;
    }

    public void updateAudioFileUrl(String audioFile) {
        this.audioFileUrl = audioFile;
    }

    public void updateAudioFileS3Path(String audioFileS3Path) {
        this.audioFileS3Path = audioFileS3Path;
    }

    public void updateText(String text) {
        this.text = text;
        checkText = true;
        checkAudio = true;
    }

    public void updateStep1(String allText, String sex, String lang, Double durationSilence, Long volume, Double pitch, Double speed, String audioModel, String audioModelUrl) {
        this.text = allText;
        this.sex = sex;
        this.lang = lang;
        this.durationSilence = durationSilence;
        this.volume = volume;
        this.pitch = pitch;
        this.speed = speed;
        this.audioModel = audioModel;
        this.audioModelUrl = audioModelUrl;
        this.audioUpload = false;
        this.audioMerge = false;
        this.checkText = true;
        this.checkAudio = false;
    }

    public void updateAudioInfos(List<AudioInfo> audioInfos) {
        if (this.audioInfos.size() != 0) {
            this.audioInfos.clear();
        }
        this.audioInfos.addAll(audioInfos);
    }

    public void updateAudioOriginName(String originalFilename) {
        this.audioFileOriginName = originalFilename;
    }

    public void updateAudioUploadSuccess(String originalFilename, String filePath, String audioFileUrl, String downloadUrl) {
        updateAudioUpload(true);
        updateAudioMerge(true);
        checkText = true;
        checkAudio = true;
        updateAudioOriginName(originalFilename);
        updateAudioFileS3Path(filePath);
        updateAudioFileUrl(audioFileUrl);
        this.downloadAudioUrl = downloadUrl;
        updateText(" ");
    }

    public void updateBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public void updateDownloadUrl(String downloadAudioUrl) {
        this.downloadAudioUrl = downloadAudioUrl;
    }

    public void updateCheckAvatar(boolean isCheckAvatar) {
        this.checkAvatar = isCheckAvatar;
    }

    public void updateCheckTextAndAudio() {
        this.checkText = false;
        this.checkAudio = false;
    }
}
