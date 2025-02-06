package com.nettruyen.comic.mapper;

import com.nettruyen.comic.dto.request.GenerateAddedRequest;
import com.nettruyen.comic.dto.response.GenerateAddedResponse;
import com.nettruyen.comic.entity.GenerateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenerateMapper {

    // Không map danh sách stories, sẽ map ở service
    @Mapping(target = "stories", ignore = true)
    GenerateEntity toEntity(GenerateAddedRequest request);

    // Không map danh sách stories, sẽ map ở service
    @Mapping(target = "stories", ignore = true)
    GenerateAddedResponse toResponse(GenerateEntity entity);
}
