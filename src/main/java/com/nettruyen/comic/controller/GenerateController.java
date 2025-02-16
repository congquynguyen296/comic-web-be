package com.nettruyen.comic.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.generate.GenerateAddedRequest;
import com.nettruyen.comic.dto.request.generate.GenerateUpdateRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.story.GenerateResponse;
import com.nettruyen.comic.service.IGenerateService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/generate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenerateController {

    IGenerateService generateService;

    @PostMapping()
    ApiResponse<GenerateResponse> createStory(@RequestBody @Valid GenerateAddedRequest generateAddedRequest) {
        return ApiResponse.<GenerateResponse>builder()
                .code(200)
                .result(generateService.createGenerate(generateAddedRequest))
                .build();
    }

    @PutMapping()
    ApiResponse<GenerateResponse> updateStory(@RequestBody @Valid GenerateUpdateRequest generateUpdateRequest) {
        return ApiResponse.<GenerateResponse>builder()
                .code(200)
                .result(generateService.updateGenerate(generateUpdateRequest))
                .build();
    }

    @DeleteMapping()
    ApiResponse<Void> deleteStory(@RequestParam String code) {
        generateService.deleteGenerate(code);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted generate completed")
                .build();
    }

    @GetMapping()
    ApiResponse<List<GenerateResponse>> getAllGenerates() {

        return ApiResponse.<List<GenerateResponse>>builder()
                .code(200)
                .result(generateService.getAllGenerate())
                .build();
    }
}
