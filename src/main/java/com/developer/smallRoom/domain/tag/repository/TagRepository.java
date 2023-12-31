package com.developer.smallRoom.domain.tag.repository;

import com.developer.smallRoom.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByName(String name);

    boolean existsByName(String name);
}
