package com.aipark.jena.domain.avatarCategory;

import com.aipark.jena.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClothesRepository extends JpaRepository<Clothes,Long> {
    List<Clothes> findAllByAvatar(Avatar avatar);
}
