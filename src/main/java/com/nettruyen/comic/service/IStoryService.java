package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.response.StoryAddedResponse;

public interface IStoryService {

    StoryAddedResponse createStory(StoryAddedRequest storyAddedRequest);

    StoryAddedResponse getStoryById(String storyId);
}
