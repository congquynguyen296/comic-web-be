package com.nettruyen.comic.service.Impl;


import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import com.nettruyen.comic.dto.request.chapter.ChapterUpdateRequest;
import com.nettruyen.comic.dto.response.story.ChapterResponse;
import com.nettruyen.comic.service.IChapterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterService implements IChapterService {


    @Override
    public ChapterResponse createChapter(ChapterCreationRequest request) {
        return null;
    }

    @Override
    public ChapterResponse updateChapter(ChapterUpdateRequest request) {
        return null;
    }

    @Override
    public ChapterResponse getChapterByChapterNumber(String chapterNumber) {
        return null;
    }

    @Override
    public List<ChapterResponse> getAllChapters() {
        return List.of();
    }

    @Override
    public Page<ChapterResponse> getAllChapters(Pageable pageable) {
        return null;
    }
}
