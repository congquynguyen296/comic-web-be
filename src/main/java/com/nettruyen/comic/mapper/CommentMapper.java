package com.nettruyen.comic.mapper;

import com.nettruyen.comic.dto.request.other.CommentRequest;
import com.nettruyen.comic.dto.response.other.CommentResponse;
import com.nettruyen.comic.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "chapterId", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "replies", ignore = true)
    CommentResponse toResponse(CommentEntity entity);
}
