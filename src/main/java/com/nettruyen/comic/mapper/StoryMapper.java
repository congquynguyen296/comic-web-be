package com.nettruyen.comic.mapper;

import com.nettruyen.comic.dto.request.story.StoryAddedRequest;
import com.nettruyen.comic.dto.request.story.StoryUpdateRequest;
import com.nettruyen.comic.dto.response.story.StoryResponse;
import com.nettruyen.comic.entity.StoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StoryMapper {

    @Mapping(target = "generates", ignore = true)
    StoryEntity toEntity(StoryAddedRequest request);

    @Mapping(target = "generates", ignore = true)
    StoryResponse toResponse(StoryEntity entity);

    @Mapping(target = "generates", ignore = true)
    void updateEntity(@MappingTarget StoryEntity entity, StoryUpdateRequest request);
}
