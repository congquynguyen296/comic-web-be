package com.nettruyen.comic.repository.internal;

import com.nettruyen.comic.entity.StoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStoryRepository extends JpaRepository<StoryEntity, String> {

    StoryEntity findByTitle(String title);

    StoryEntity findByCode(String code);
}
