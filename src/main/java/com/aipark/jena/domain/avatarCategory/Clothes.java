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
@Table(name = "CLOTHES")
@Entity
public class Clothes {
    @Id
    @GeneratedValue
    @Column(name = "clothes_id")
    private Long id;

    @Column
    private String clothesUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Avatar avatarId;
}
