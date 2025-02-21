package com.nettruyen.comic.repository;

import com.nettruyen.comic.entity.ChapterEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IChapterRepository extends JpaRepository<ChapterEntity, String> {

    ChapterEntity findByChapterCode(String code);

    @Query("SELECT c " +
            "FROM tbl_chapter c " +
            "WHERE c.story.code = :storyCode " +
            "AND c.chapterCode = :chapterCode")
    ChapterEntity findByStoryCodeAndChapterCode(@Param("storyCode") String storyCode,
                                                @Param("chapterCode") String chapterCode);
}
