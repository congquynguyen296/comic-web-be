package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.constant.StatusEnum;
import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.request.StoryUpdateRequest;
import com.nettruyen.comic.dto.response.StoryResponse;
import com.nettruyen.comic.entity.GenerateEntity;
import com.nettruyen.comic.entity.StoryEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.StoryMapper;
import com.nettruyen.comic.repository.IGenerateRepository;
import com.nettruyen.comic.repository.IStoryRepository;
import com.nettruyen.comic.service.IStoryService;
import com.nettruyen.comic.util.ConvertorUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    public StoryResponse createStory(StoryAddedRequest storyAddedRequest) {
        if (storyRepository.findByTitle(storyAddedRequest.getTitle()) != null)
            throw new AppException(ErrorCode.STORY_ALREADY_EXITS);

        StoryEntity storyEntity = storyMapper.toEntity(storyAddedRequest);
        storyEntity.setCode(ConvertorUtil.convertNameToCode(storyAddedRequest.getTitle()));

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
            StoryResponse result = storyMapper.toResponse(newStory);

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
    public StoryResponse getStoryById(String storyId) {
        if (storyId == null)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        try {
            StoryEntity existedStory = storyRepository.findById(storyId)
                    .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED));

            StoryResponse result = storyMapper.toResponse(existedStory);
            result.setGenerates(existedStory.getGenerates().stream()
                    .map(GenerateEntity::getName)
                    .collect(Collectors.toSet()));
            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public StoryResponse updateStory(StoryUpdateRequest storyUpdateRequest) {
        try {

            StoryEntity storyExisted = storyRepository.findById(storyUpdateRequest.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.GENERATE_NOT_EXITS));

            if (storyExisted != null) {

                // Update info
                storyMapper.updateEntity(storyExisted, storyUpdateRequest);
                storyExisted.setGenerates(storyUpdateRequest.getGenerates().stream()
                                .map(generateRepository::findByCode)
                        .collect(Collectors.toSet()));
                storyExisted.setCode(ConvertorUtil.convertNameToCode(storyUpdateRequest.getTitle()));

                storyRepository.save(storyExisted);

                StoryResponse result = storyMapper.toResponse(storyExisted);
                result.setGenerates(storyExisted.getGenerates().stream()
                        .map(GenerateEntity::getName)
                        .collect(Collectors.toSet()));

                return result;
            }
            return null;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public void deleteStory(String storyId) {
        try {

            StoryEntity storyExisted = storyRepository.findById(storyId)
                            .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_EXITS));

            storyExisted.setStatus(StatusEnum.OFF);
            storyRepository.save(storyExisted);

            log.info("Story is deleted {}", storyExisted.getTitle());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public List<StoryResponse> getAllStory() {

        try {

            List<StoryEntity> stories = storyRepository.findAll();
            if (!stories.isEmpty()) {

                return stories.stream().map(story -> {

                    StoryResponse storyResponse = storyMapper.toResponse(story);

                    // Map name generate
                    storyResponse.setGenerates(story.getGenerates().stream()
                            .map(GenerateEntity::getName)
                            .collect(Collectors.toSet()));

                    return storyResponse;

                }).toList();

            } else {
                log.error("Stories is empty");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }

        return List.of();
    }

//    @Override
//    public List<StoryResponse> getAllStory(int pageNo, int pageSize) {
//
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        Page<StoryEntity> storyEntities = storyRepository.findAll(pageable);
//
//        List<StoryResponse> storyResponses = new ArrayList<>();
//        for (StoryEntity story : storyEntities) {
//
//            StoryResponse response = storyMapper.toResponse(story);
//
//            Set<String> generateNames = new HashSet<>();
//            for (GenerateEntity generate : story.getGenerates()) {
//                generateNames.add(generate.getName());
//            }
//
//            response.setGenerates(generateNames);
//
//            storyResponses.add(response);
//        }
//
//        return storyResponses;
//    }


    @Override
    public Page<StoryResponse> getAllStory(int pageNo, int pageSize) {

        int adjustedPageNo = (pageNo > 0) ? pageNo - 1 : 0;

        Pageable pageable = PageRequest.of(adjustedPageNo, pageSize);
        Page<StoryEntity> stories = storyRepository.findAll(pageable);

        return stories.map(story -> {

            StoryResponse storyResponse = storyMapper.toResponse(story);

            // Set generate cho từng storyResponse
            storyResponse.setGenerates(
                    story.getGenerates().stream()
                            .map(GenerateEntity::getName)
                            .collect(Collectors.toSet())
            );

            return storyResponse;
        });

        // Có thể tận dụng .parallelStream() để tăng tốc
    }
}