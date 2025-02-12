package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.story.StoryAddedRequest;
import com.nettruyen.comic.dto.request.story.StoryUpdateRequest;
import com.nettruyen.comic.dto.response.StoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IStoryService {

    StoryResponse createStory(StoryAddedRequest storyAddedRequest);

    StoryResponse getStoryById(String storyId);

    StoryResponse updateStory(StoryUpdateRequest storyUpdateRequest);

    void deleteStory(String storyId);

    List<StoryResponse> getAllStory();

    Page<StoryResponse> getAllStory(int pageNo, int pageSize);
}
