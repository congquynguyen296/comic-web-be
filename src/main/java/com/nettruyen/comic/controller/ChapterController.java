package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.chapter.ChapterResponse;
import com.nettruyen.comic.service.IChapterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChapterController {

    IChapterService chapterService;


    @GetMapping("/api/chapters")
    ApiResponse<Page<ChapterResponse>> getAllChapters(@RequestParam("pageNo") String pageNo,
                                                      @RequestParam("pageSize") String pageSize) {


        return ApiResponse.<Page<ChapterResponse>>builder()
                .code(200)
                .result(chapterService.getAllChapters(Integer.parseInt(pageNo), Integer.parseInt(pageSize)))
                .build();
    }

    @GetMapping("/api/chapter")
    ApiResponse<ChapterResponse> getChapterByChapterNumber(@RequestParam("story-code") String storyCode,
                                                           @RequestParam("chapter-number") String chapterNumber) {

        return ApiResponse.<ChapterResponse>builder()
                .code(200)
                .result(chapterService.getChapterByStoryCodeAndChapterNumber(storyCode, Integer.parseInt(chapterNumber)))
                .build();
    }

    @PostMapping("/api/chapter")
    ApiResponse<ChapterResponse> createChapter(@RequestBody ChapterCreationRequest request) {

        return ApiResponse.<ChapterResponse>builder()
                .code(200)
                .result(chapterService.createChapter(request))
                .build();
    }
}
