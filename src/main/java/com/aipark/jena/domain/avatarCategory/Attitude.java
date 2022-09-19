package com.aipark.jena.domain.avatarCategory;

import com.aipark.jena.domain.Avatar;
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
@Table(name = "ATTITUDE")
@Entity
public class Attitude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attitude_id")
    private Long id;

    @Column
    private String attitudeUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;
}
