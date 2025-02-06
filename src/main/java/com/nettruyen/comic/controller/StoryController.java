package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.StoryAddedResponse;
import com.nettruyen.comic.service.IStoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class StoryController {

    IStoryService storyService;

    @PostMapping()
    ApiResponse<StoryAddedResponse> createStory(@RequestBody @Valid StoryAddedRequest storyAddedRequest) {
        return ApiResponse.<StoryAddedResponse>builder()
                .code(200)
                .result(storyService.createStory(storyAddedRequest))
                .build();
    }
}
