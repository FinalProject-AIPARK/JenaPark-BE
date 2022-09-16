package com.aipark.jena.domain.avatarCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttitudeRepository extends JpaRepository<Attitude,Long> {
    List<Attitude> findAllByAvatarId(Long avatarId);
}
