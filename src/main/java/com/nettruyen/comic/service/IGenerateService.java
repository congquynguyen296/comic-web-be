package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.generate.GenerateAddedRequest;
import com.nettruyen.comic.dto.request.generate.GenerateUpdateRequest;
import com.nettruyen.comic.dto.response.story.GenerateResponse;

import java.util.List;

public interface IGenerateService {

    GenerateResponse createGenerate(GenerateAddedRequest request);

    GenerateResponse updateGenerate(GenerateUpdateRequest request);

    void deleteGenerate(String storyId);

    List<GenerateResponse> getAllGenerate();
}
