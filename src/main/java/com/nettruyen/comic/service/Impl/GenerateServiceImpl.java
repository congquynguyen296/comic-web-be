package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.dto.request.generate.GenerateAddedRequest;
import com.nettruyen.comic.dto.request.generate.GenerateUpdateRequest;
import com.nettruyen.comic.dto.response.story.GenerateResponse;
import com.nettruyen.comic.entity.GenerateEntity;
import com.nettruyen.comic.entity.StoryEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.GenerateMapper;
import com.nettruyen.comic.repository.internal.IGenerateRepository;
import com.nettruyen.comic.repository.internal.IStoryRepository;
import com.nettruyen.comic.service.IGenerateService;
import com.nettruyen.comic.util.ConvertorUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenerateServiceImpl implements IGenerateService {

    IStoryRepository storyRepository;
    IGenerateRepository generateRepository;
    GenerateMapper generateMapper;


    @Override
    public GenerateResponse createGenerate(GenerateAddedRequest generateAddedRequest) {

        if (generateRepository.findByName(generateAddedRequest.getName()) != null)
            throw new AppException(ErrorCode.GENERATE_ALREADY_EXITS);

        GenerateEntity generateEntity = generateMapper.toEntity(generateAddedRequest);
        generateEntity.setCode(ConvertorUtil.convertNameToCode(generateAddedRequest.getName()));

        try {

            // Map các truyện vào entity thể loại
            Set<StoryEntity> storiesOfEntity = new HashSet<>();
            if (generateAddedRequest.getStories() != null) {
                storiesOfEntity = generateAddedRequest.getStories().stream()
                        .map(storyId -> storyRepository.findById(storyId)
                                .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_EXITS)))
                        .collect(Collectors.toSet());
            }

            generateEntity.setStories(storiesOfEntity);
            GenerateEntity newGenerate = generateRepository.save(generateEntity);
            GenerateResponse result = generateMapper.toResponse(newGenerate);

            // Map lại tên các truyện thuộc thể loại này
            Set<String> storiesOfResponse = newGenerate.getStories().stream()
                    .map(StoryEntity::getTitle)
                    .collect(Collectors.toSet());
            result.setStories(storiesOfResponse);

            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public GenerateResponse updateGenerate(GenerateUpdateRequest request) {

        try {

            GenerateEntity generateExisted = generateRepository.findById(request.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.GENERATE_NOT_EXITS));

            if (generateExisted != null) {

                // Giữ lại list story cũ
                Set<StoryEntity> oldStories = generateExisted.getStories();

                // Update info
                generateMapper.updateEntity(generateExisted, request);
                generateExisted.setStories(oldStories);
                generateExisted.setCode(ConvertorUtil.convertNameToCode(generateExisted.getName()));

                generateRepository.save(generateExisted);

                GenerateResponse result = generateMapper.toResponse(generateExisted);
                result.setStories(generateExisted.getStories().stream()
                        .map(StoryEntity::getTitle)
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
    public void deleteGenerate(String storyId) {

        try {

            GenerateEntity generateExisted = generateRepository.findById(storyId)
                    .orElseThrow(() -> new AppException(ErrorCode.GENERATE_NOT_EXITS));

            generateRepository.delete(generateExisted);

            log.info("Deleted generate {}", generateExisted.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public List<GenerateResponse> getAllGenerate() {

        try {

            List<GenerateEntity> generates = generateRepository.findAll();
            if (!generates.isEmpty()) {

                return generates.stream().map(generate -> {
                    GenerateResponse generateResponse = generateMapper.toResponse(generate);

                    // Lấy tất cả code theo từng thể loại truyện
                    // Hiện chỉ cần lấy code để xem (tiện cho việc get story theo code sau này)
                    generateResponse.setStories(generate.getStories().stream()
                            .map(StoryEntity::getCode)
                            .collect(Collectors.toSet()));

                    return generateResponse;
                }).toList();

            } else {
                log.error("Empty generate list");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }

        return List.of();
    }
}
