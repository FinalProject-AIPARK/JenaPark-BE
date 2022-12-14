package com.aipark.jena.domain.avatarCategory;

import com.aipark.jena.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessoriesRepository extends JpaRepository<Accessories,Long> {
    List<Accessories> findAllByAvatar(Avatar avatar);

    boolean existsByIdAndAvatar(Long id,Avatar avatar);
}
