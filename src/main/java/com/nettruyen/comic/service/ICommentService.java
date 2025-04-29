package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.other.CommentRequest;
import com.nettruyen.comic.dto.response.other.CommentResponse;

import java.util.List;

public interface ICommentService {
    CommentResponse createComment(CommentRequest commentRequest);
    List<CommentResponse> getAllCommentForChapter(String chapterId);
}
