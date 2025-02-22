package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import com.nettruyen.comic.dto.request.chapter.ChapterUpdateRequest;
import com.nettruyen.comic.dto.response.chapter.ChapterResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface IChapterService {

    ChapterResponse createChapter(ChapterCreationRequest request);

    ChapterResponse updateChapter(ChapterUpdateRequest request);

    Page<ChapterResponse> getAllChapters(int pageNo, int pageSize);

    List<ChapterResponse> getAllChapters();

    ChapterResponse getChapterByStoryCodeAndChapterNumber(String storyCode, Integer chapterNumber);
}
