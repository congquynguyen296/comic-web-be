package com.nettruyen.comic.service.Impl;


import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import com.nettruyen.comic.dto.request.chapter.ChapterUpdateRequest;
import com.nettruyen.comic.dto.response.chapter.ChapterResponse;
import com.nettruyen.comic.entity.ChapterEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.ChapterMapper;
import com.nettruyen.comic.mapper.StoryMapper;
import com.nettruyen.comic.repository.IChapterRepository;
import com.nettruyen.comic.repository.IStoryRepository;
import com.nettruyen.comic.service.IChapterService;
import com.nettruyen.comic.util.ScraperData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterService implements IChapterService {

    IChapterRepository chapterRepository;
    IStoryRepository storyRepository;
    ChapterMapper chapterMapper;
    StoryMapper storyMapper;


    @Override
    public ChapterResponse createChapter(ChapterCreationRequest request) {

        // Prepare data
        var chapterScraped = ScraperData.scraperChapter(request.getUrl());
        chapterScraped.setStoryId(request.getStoryId());


        var storyEntityContainChapter = storyRepository.findById(chapterScraped.getStoryId());
        if (storyEntityContainChapter.isEmpty())
            throw new AppException(ErrorCode.CHAPTER_NOT_EXITS);

        // Check xem chapter đã tồn tại chưa qua chapter code
        if (chapterRepository.findByChapterNumber(chapterScraped.getChapterNumber()) != null)
            throw new AppException(ErrorCode.CHAPTER_ALREADY_EXITS);

        try {
            ChapterEntity chapterEntity = chapterMapper.toEntity(chapterScraped);
            chapterEntity.setStory(storyEntityContainChapter.get());


            chapterRepository.save(chapterEntity);

            ChapterResponse result = chapterMapper.toResponse(chapterEntity);
            result.setStory(storyMapper.toResponse(chapterEntity.getStory()));

            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.STORY_ALREADY_EXITS);
        }
    }

    @Override
    public ChapterResponse updateChapter(ChapterUpdateRequest request) {

        return null;
    }

    @Override
    public List<ChapterResponse> getAllChapters() {

        try {
            List<ChapterEntity> chapters = chapterRepository.findAll();

            if (!chapters.isEmpty()) {
                return chapters.stream().map(chapter -> {
                   ChapterResponse chapterResponse = chapterMapper.toResponse(chapter);

                   var storyContainChapter = storyRepository.findById(chapter.getId());
                   if (storyContainChapter.isPresent()) {
                       chapterResponse.setStory(storyMapper.toResponse(storyContainChapter.get()));
                   } else {
                       chapterResponse.setStory(null);
                   }

                   return chapterResponse;
                }).toList();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }

        return List.of();
    }

    @Override
    public Page<ChapterResponse> getAllChapters(int pageNo, int pageSize) {
        int adjustPageNo = (pageNo > 0) ? pageNo - 1 : 0;


        Pageable pageable = PageRequest.of(adjustPageNo, pageSize,
                Sort.by("chapterNumber").ascending());



        try {
            Page<ChapterEntity> chapterEntities= chapterRepository.findAll(pageable);
            return chapterEntities.map(chapterEntity -> {
                ChapterResponse chapterResponse = chapterMapper.toResponse(chapterEntity);

                var storyContainChapter = storyRepository.findById(chapterEntity.getStory().getId());
                if (storyContainChapter.isPresent()) {
                    chapterResponse.setStory(storyMapper.toResponse(storyContainChapter.get()));
                } else {
                    chapterResponse.setStory(null);
                }

                return chapterResponse;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChapterResponse getChapterByStoryCodeAndChapterNumber(String storyCode, Integer chapterNumber) {
        if (storyCode == null || chapterNumber == null)
            throw new AppException(ErrorCode.UNCATEGORIZED);

        try {

            ChapterEntity chapterExisted = chapterRepository.findByStoryCodeAndChapterNumber(storyCode, chapterNumber);
            if (chapterExisted == null) throw new AppException(ErrorCode.CHAPTER_NOT_EXITS);

            ChapterResponse result = chapterMapper.toResponse(chapterExisted);
            var storyContainChapter = storyRepository.findById(chapterExisted.getStory().getId());
            if (storyContainChapter.isPresent()) {
                result.setStory(storyMapper.toResponse(storyContainChapter.get()));
            } else {
                result.setStory(null);
            }

            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    private int extractNumberFromChapterNumber(String chapterNumber) {
        String numberPart = chapterNumber.replaceAll("[^0-9]", ""); // Loại bỏ tất cả ký tự không phải số
        return Integer.parseInt(numberPart);
    }
}
