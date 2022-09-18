package com.aipark.jena.domain.avatarCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClothesRepository extends JpaRepository<Clothes,Long> {
    List<Clothes> findAllByAvatarId(Long avatarId);
}
