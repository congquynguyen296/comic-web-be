package com.nettruyen.comic.repository;

import com.nettruyen.comic.entity.ChapterEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IChapterRepository extends JpaRepository<ChapterEntity, String> {

    ChapterEntity findByChapterNumber(Integer chapterNumber);

    Page<ChapterEntity> findByStoryCode(String storyCode, Pageable pageable);

    @Query("SELECT c " +
            "FROM tbl_chapter c " +
            "WHERE c.story.code = :storyCode " +
            "AND c.chapterNumber = :chapterNumber")
    ChapterEntity findByStoryCodeAndChapterNumber(@Param("storyCode") String storyCode,
                                                @Param("chapterNumber") Integer chapterNumber);
}
