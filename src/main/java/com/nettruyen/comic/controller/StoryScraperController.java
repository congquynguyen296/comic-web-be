package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.wrapper.ScraperChapterRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.service.Impl.StoryScraperService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/scraper")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoryScraperController {

    StoryScraperService storyScraperService;

    @GetMapping
    ApiResponse<String> scraperChapterContent(@RequestBody ScraperChapterRequest request) {
        return ApiResponse.<String>builder()
                .code(200)
                .result(storyScraperService.scraperChapterContent(request.getUrl()))
                .build();
    }
}
