package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.story.StoryAddedRequest;
import com.nettruyen.comic.dto.request.story.StoryUpdateRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.story.StoryResponse;
import com.nettruyen.comic.service.IStoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class StoryController {

    IStoryService storyService;

    @PostMapping("/api/story")
    ApiResponse<StoryResponse> createStory(@RequestBody @Valid StoryAddedRequest storyAddedRequest) {
        return ApiResponse.<StoryResponse>builder()
                .code(200)
                .result(storyService.createStory(storyAddedRequest))
                .build();
    }

    @PutMapping("/api/story")
    ApiResponse<StoryResponse> updateStory(@RequestBody @Valid StoryUpdateRequest storyUpdateRequest) {
        return ApiResponse.<StoryResponse>builder()
                .code(200)
                .result(storyService.updateStory(storyUpdateRequest))
                .build();
    }

    @DeleteMapping("/api/story")
    ApiResponse<Void> deleteStory(@RequestParam String code) {
        storyService.deleteStory(code);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted story completed")
                .build();
    }

    @GetMapping("/api/story/{code}")
    ApiResponse<StoryResponse> getStory(@PathVariable String code,
                                        @RequestParam("page-no") String pageNo,
                                        @RequestParam("page-size") String pageSize) {

        return ApiResponse.<StoryResponse>builder()
                .code(200)
                .result(storyService.getStoryByCodeAndPagingChapter(
                        code, Integer.parseInt(pageNo), Integer.parseInt(pageSize)))
                .build();
    }

    @GetMapping("api/story/{story-code}/total-chapters")
    int getTotalChapters(@PathVariable("story-code") String parameter) {
        return storyService.countChapterByStoryCode(parameter);
    }

    @GetMapping("/api/stories")
    ApiResponse<Map<String, Object>> getAllStory(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize) {

        // Nếu không có `pageNo` hoặc `pageSize`, trả về toàn bộ danh sách (không khuyến khích khi dữ liệu lớn)
        if (pageNo == null || pageSize == null) {
            List<StoryResponse> allStories = storyService.getAllStory();
            Map<String, Object> response = new HashMap<>();
            response.put("result", allStories);
            response.put("totalItems", allStories.size());
            return ApiResponse.<Map<String, Object>>builder()
                    .code(200)
                    .result(response)
                    .build();
        }

        // Phân trang
        Page<StoryResponse> pagedStories = storyService.getAllStory(pageNo, pageSize);
        Map<String, Object> response = new HashMap<>();
        response.put("content", pagedStories.getContent()); // Danh sách truyện trên trang hiện tại
        response.put("totalItems", pagedStories.getTotalElements()); // Tổng số truyện
        response.put("totalPages", pagedStories.getTotalPages()); // Tổng số trang
        return ApiResponse.<Map<String, Object>>builder()
                .code(200)
                .result(response)
                .build();
    }

}
