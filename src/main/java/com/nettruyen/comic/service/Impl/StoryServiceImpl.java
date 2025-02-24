package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.constant.StatusEnum;
import com.nettruyen.comic.dto.request.story.StoryAddedRequest;
import com.nettruyen.comic.dto.request.story.StoryUpdateRequest;
import com.nettruyen.comic.dto.response.story.StoryResponse;
import com.nettruyen.comic.entity.ChapterEntity;
import com.nettruyen.comic.entity.GenerateEntity;
import com.nettruyen.comic.entity.StoryEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.StoryMapper;
import com.nettruyen.comic.repository.internal.IChapterRepository;
import com.nettruyen.comic.repository.internal.IGenerateRepository;
import com.nettruyen.comic.repository.internal.IStoryRepository;
import com.nettruyen.comic.service.IStoryService;
import com.nettruyen.comic.util.ConvertorUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
    IChapterRepository chapterRepository;
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

            result.setChapters(existedStory.getChapters().stream()
                    .sorted(Comparator.comparingInt(ChapterEntity::getChapterNumber))
                    .map(chapterEntity -> ConvertorUtil.convertToChapterComponentResponse(chapterEntity))
                    .toList());
            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public StoryResponse getStoryByCodeAndPagingChapter(String code, int pageNo, int pageSize) {
        if (code == null) {
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }

        try {
            // Lấy thông tin truyện
            StoryEntity existedStory = storyRepository.findByCode(code);
            if (existedStory == null) {
                throw new AppException(ErrorCode.STORY_NOT_EXITS);
            }

            // Lấy danh sách chapter với phân trang và sắp xếp
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("chapterNumber").ascending());
            Page<ChapterEntity> chapterPage = chapterRepository.findByStoryCode(code, pageable);

            // Chuyển đổi sang StoryResponse
            StoryResponse result = storyMapper.toResponse(existedStory);
            result.setGenerates(existedStory.getGenerates().stream()
                    .map(GenerateEntity::getName)
                    .collect(Collectors.toSet()));

            // Chuyển đổi và sắp xếp chapter (list sẽ giữ thứ tự)
            result.setChapters(chapterPage.getContent().stream()
                    .map(ConvertorUtil::convertToChapterComponentResponse)
                    .collect(Collectors.toList()));

            return result;

        } catch (Exception e) {
            log.error("Error fetching story by code: {}", e.getMessage());
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

//                result.setChapters(storyExisted.getChapters().stream()
//                        .map(chapterEntity -> ConvertorUtil.convertToChapterComponentResponse(chapterEntity))
//                        .collect(Collectors.toSet()));

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

                    // Map chapter (Get number only)
//                    storyResponse.setChapters(story.getChapters().stream()
//                            .map(chapterEntity -> ConvertorUtil.convertToChapterComponentResponse(chapterEntity))
//                            .collect(Collectors.toSet()));

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


    @Override
    public Page<StoryResponse> getAllStory(int pageNo, int pageSize) {

        int adjustedPageNo = (pageNo > 0) ? pageNo - 1 : 0;

        try {
            Pageable pageable = PageRequest.of(adjustedPageNo, pageSize);
            Page<StoryEntity> stories = storyRepository.findAll(pageable);

            return stories.map(story -> {

                StoryResponse storyResponse = storyMapper.toResponse(story);

                // Set generate cho từng storyResponse
                storyResponse.setGenerates(story.getGenerates().stream()
                        .map(GenerateEntity::getName)
                        .collect(Collectors.toSet()));

                // Cân nhắc dùng hoặc không
                // Có thể để khi vào chapter thì mới dùng
                storyResponse.setChapters(story.getChapters().stream()
                        .map(chapterEntity -> ConvertorUtil.convertToChapterComponentResponse(chapterEntity))
                        .toList());

                return storyResponse;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }


        // Có thể tận dụng .parallelStream() để tăng tốc
    }

    @Override
    public int countChapterByStoryCode(String storyCode) {
        try {
            StoryEntity storyExisted = storyRepository.findByCode(storyCode);
            return storyExisted.getChapters().size();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0;
    }
}