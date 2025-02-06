package com.nettruyen.comic.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.GenerateAddedRequest;
import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.GenerateAddedResponse;
import com.nettruyen.comic.dto.response.StoryAddedResponse;
import com.nettruyen.comic.service.IGenerateService;
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
@RequestMapping("/generate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenerateController {

    IGenerateService generateService;

    @PostMapping()
    ApiResponse<GenerateAddedResponse> createStory(@RequestBody @Valid GenerateAddedRequest generateAddedRequest) {
        return ApiResponse.<GenerateAddedResponse>builder()
                .code(200)
                .result(generateService.createGenerate(generateAddedRequest))
                .build();
    }
}
