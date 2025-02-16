package com.nettruyen.comic.mapper;

import com.nettruyen.comic.dto.request.generate.GenerateAddedRequest;
import com.nettruyen.comic.dto.request.generate.GenerateUpdateRequest;
import com.nettruyen.comic.dto.response.story.GenerateResponse;
import com.nettruyen.comic.entity.GenerateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenerateMapper {

    // Không map danh sách stories, sẽ map ở service
    @Mapping(target = "stories", ignore = true)
    GenerateEntity toEntity(GenerateAddedRequest request);

    // Không map danh sách stories, sẽ map ở service
    @Mapping(target = "stories", ignore = true)
    GenerateResponse toResponse(GenerateEntity entity);

    @Mapping(target = "stories", ignore = true)
    void updateEntity(@MappingTarget GenerateEntity entity, GenerateUpdateRequest request);
}
