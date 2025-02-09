package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.request.StoryUpdateRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.StoryResponse;
import com.nettruyen.comic.service.IStoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class StoryController {

    IStoryService storyService;

    @PostMapping()
    ApiResponse<StoryResponse> createStory(@RequestBody @Valid StoryAddedRequest storyAddedRequest) {
        return ApiResponse.<StoryResponse>builder()
                .code(200)
                .result(storyService.createStory(storyAddedRequest))
                .build();
    }

    @PutMapping()
    ApiResponse<StoryResponse> updateStory(@RequestBody @Valid StoryUpdateRequest storyUpdateRequest) {
        return ApiResponse.<StoryResponse>builder()
                .code(200)
                .result(storyService.updateStory(storyUpdateRequest))
                .build();
    }

    @DeleteMapping()
    ApiResponse<Void> deleteStory(@RequestParam String code) {
        storyService.deleteStory(code);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted story completed")
                .build();
    }

    @GetMapping
    ApiResponse<List<StoryResponse>> getAllStory(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "5", required = false) int pageSize) {

        return ApiResponse.<List<StoryResponse>>builder()
                .code(200)
                .result(storyService.getAllStory(pageNo, pageSize))
                .build();
    }

    @GetMapping()
    ApiResponse<List<StoryResponse>> getAllStory() {

        return ApiResponse.<List<StoryResponse>>builder()
                .code(200)
                .result(storyService.getAllStory())
                .build();
    }
}
