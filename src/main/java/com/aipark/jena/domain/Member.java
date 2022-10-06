package com.aipark.jena.domain;

import com.aipark.jena.enums.Authority;
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
@Table(name = "MEMBER")
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String profileImg;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Project> projects = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Video> videos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void addProject(Project project) {
        projects.add(project);
        if (project.getMember() != this) {
            project.setMember(this);
        }
    }

    public void addVideo(Video video) {
        videos.add(video);
        if (video.getMember() != this) {
            video.setMember(this);
        }
    }
}
