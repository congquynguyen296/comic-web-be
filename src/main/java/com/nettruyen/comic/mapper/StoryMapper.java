package com.nettruyen.comic.mapper;

import com.nettruyen.comic.dto.request.StoryAddedRequest;
import com.nettruyen.comic.dto.response.StoryAddedResponse;
import com.nettruyen.comic.entity.StoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoryMapper {

    @Mapping(target = "generates", ignore = true)
    StoryEntity toEntity(StoryAddedRequest request);

    @Mapping(target = "generates", ignore = true)
    StoryAddedResponse toResponse(StoryEntity entity);
}
