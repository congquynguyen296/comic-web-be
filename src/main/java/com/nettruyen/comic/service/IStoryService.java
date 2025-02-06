package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.request.StoryUpdateRequest;
import com.nettruyen.comic.dto.response.StoryResponse;

public interface IStoryService {

    StoryResponse createStory(StoryAddedRequest storyAddedRequest);

    StoryResponse getStoryById(String storyId);

    StoryResponse updateStory(StoryUpdateRequest storyUpdateRequest);

    void deleteStory(String storyId);
}
