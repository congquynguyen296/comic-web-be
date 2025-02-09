package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.request.StoryUpdateRequest;
import com.nettruyen.comic.dto.response.StoryResponse;

import java.util.List;

public interface IStoryService {

    StoryResponse createStory(StoryAddedRequest storyAddedRequest);

    StoryResponse getStoryById(String storyId);

    StoryResponse updateStory(StoryUpdateRequest storyUpdateRequest);

    void deleteStory(String storyId);

    List<StoryResponse> getAllStory();

    List<StoryResponse> getAllStory(int pageNo, int pageSize);
}
