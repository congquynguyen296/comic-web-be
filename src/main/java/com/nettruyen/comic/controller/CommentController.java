package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.other.CommentRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.other.CommentResponse;
import com.nettruyen.comic.service.ICommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentController {

    ICommentService commentService;

    @PostMapping("/api/comment")
    ApiResponse<CommentResponse> createComment(@RequestBody CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .code(200)
                .result(commentService.createComment(request))
                .build();
    }

    @GetMapping("api/comments/{chapter-id}")
    ApiResponse<List<CommentResponse>> getAllComments(@PathVariable("chapter-id") String chapterId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .code(200)
                .result(commentService.getAllCommentForChapter(chapterId))
                .build();
    }
}
