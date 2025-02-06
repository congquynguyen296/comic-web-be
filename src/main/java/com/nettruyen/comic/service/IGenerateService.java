package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.GenerateAddedRequest;
import com.nettruyen.comic.dto.request.GenerateUpdateRequest;
import com.nettruyen.comic.dto.response.GenerateResponse;

public interface IGenerateService {

    GenerateResponse createGenerate(GenerateAddedRequest request);

    GenerateResponse updateGenerate(GenerateUpdateRequest request);

    void deleteGenerate(String storyId);
}
