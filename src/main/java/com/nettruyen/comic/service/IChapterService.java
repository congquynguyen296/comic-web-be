package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import com.nettruyen.comic.dto.request.chapter.ChapterUpdateRequest;
import com.nettruyen.comic.dto.response.story.ChapterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IChapterService {

    ChapterResponse createChapter(ChapterCreationRequest request);

    ChapterResponse updateChapter(ChapterUpdateRequest request);

    ChapterResponse getChapterByChapterNumber(String chapterNumber);

    List<ChapterResponse> getAllChapters();

    Page<ChapterResponse> getAllChapters(Pageable pageable);
}
