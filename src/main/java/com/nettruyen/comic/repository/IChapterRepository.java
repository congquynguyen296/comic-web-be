package com.nettruyen.comic.repository;

import com.nettruyen.comic.entity.ChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IChapterRepository extends JpaRepository<ChapterEntity, String> {

    ChapterEntity findByChapterNumber(String chapterNumber);

    ChapterEntity findByTitle(String title);
}
