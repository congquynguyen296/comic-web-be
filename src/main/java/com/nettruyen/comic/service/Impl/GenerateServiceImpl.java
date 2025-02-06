package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.dto.request.GenerateAddedRequest;
import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.response.GenerateAddedResponse;
import com.nettruyen.comic.dto.response.StoryAddedResponse;
import com.nettruyen.comic.entity.GenerateEntity;
import com.nettruyen.comic.entity.StoryEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.GenerateMapper;
import com.nettruyen.comic.mapper.StoryMapper;
import com.nettruyen.comic.repository.IGenerateRepository;
import com.nettruyen.comic.repository.IStoryRepository;
import com.nettruyen.comic.service.IGenerateService;
import com.nettruyen.comic.service.IStoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.HashSet;
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
    public GenerateAddedResponse createGenerate(GenerateAddedRequest generateAddedRequest) {

        if (generateRepository.findByName(generateAddedRequest.getName()) != null)
            throw new AppException(ErrorCode.GENERATE_ALREADY_EXITS);

        GenerateEntity generateEntity = generateMapper.toEntity(generateAddedRequest);
        generateEntity.setCode(convertNameToCode(generateAddedRequest.getName()));

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
            GenerateAddedResponse result = generateMapper.toResponse(newGenerate);

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

    private String convertNameToCode(String name) {

        String withoutAccents = Normalizer.normalize(name, Normalizer.Form.NFD);
        withoutAccents = withoutAccents.replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        return name.isEmpty()
                ? "unknown"
                : withoutAccents.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
