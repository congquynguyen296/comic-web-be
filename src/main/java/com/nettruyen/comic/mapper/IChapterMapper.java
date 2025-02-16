package com.nettruyen.comic.mapper;

import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import com.nettruyen.comic.entity.ChapterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IChapterMapper {

    @Mapping(target = "", ignore = true)
    ChapterEntity toEntity(ChapterCreationRequest request);
}
