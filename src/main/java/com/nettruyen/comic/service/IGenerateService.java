package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.GenerateAddedRequest;
import com.nettruyen.comic.dto.response.GenerateAddedResponse;

public interface IGenerateService {

    GenerateAddedResponse createGenerate(GenerateAddedRequest request);
}
