package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.response.StoryAddedResponse;
import com.nettruyen.comic.entity.GenerateEntity;
import com.nettruyen.comic.entity.StoryEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.StoryMapper;
import com.nettruyen.comic.repository.IGenerateRepository;
import com.nettruyen.comic.repository.IStoryRepository;
import com.nettruyen.comic.service.IStoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoryServiceImpl implements IStoryService {

    IGenerateRepository generateRepository;
    IStoryRepository storyRepository;
    StoryMapper storyMapper;


    @Override
    public StoryAddedResponse createStory(StoryAddedRequest storyAddedRequest) {
        if (storyRepository.findByTitle(storyAddedRequest.getTitle()) != null)
            throw new AppException(ErrorCode.STORY_ALREADY_EXITS);

        StoryEntity storyEntity = storyMapper.toEntity(storyAddedRequest);

        try {

            // Map generate vào story
            Set<GenerateEntity> generatesOfEntity = new HashSet<>();
            if (storyAddedRequest.getGenerates() != null) {
                generatesOfEntity = storyAddedRequest.getGenerates().stream()
                        .map(generateRepository::findByCode)
                        .collect(Collectors.toSet());
            }

            storyEntity.setGenerates(generatesOfEntity);
            StoryEntity newStory = storyRepository.save(storyEntity);
            StoryAddedResponse result = storyMapper.toResponse(newStory);

            Set<String> generatesOfResponse = newStory.getGenerates().stream()
                    .map(GenerateEntity::getName)
                    .collect(Collectors.toSet());
            result.setGenerates(generatesOfResponse);

            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public StoryAddedResponse getStoryById(String storyId) {
        if (storyId == null)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        try {
            StoryEntity existedStory = storyRepository.findById(storyId)
                    .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED));

            return storyMapper.toResponse(existedStory);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }
}
