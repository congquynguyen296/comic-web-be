package com.nettruyen.comic.mapper;

import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import com.nettruyen.comic.dto.request.chapter.ChapterUpdateRequest;
import com.nettruyen.comic.dto.response.chapter.ChapterResponse;
import com.nettruyen.comic.entity.ChapterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    @Mapping(target = "story", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ChapterEntity toEntity(ChapterCreationRequest request);

    @Mapping(target = "story", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ChapterResponse toResponse(ChapterEntity entity);

    @Mapping(target = "story", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateEntity(@MappingTarget ChapterEntity entity, ChapterUpdateRequest request);
}
