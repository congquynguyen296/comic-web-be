package com.nettruyen.comic.dto.response.chapter;

import com.nettruyen.comic.dto.response.story.StoryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChapterResponse {

    String id;

    String title;

    String content;

    Integer chapterNumber;

    // Cân nhắc có cần lấy ra story hay không
    StoryResponse story;

    // Nữa khi có comment sẽ là CommentResponse
    Set<Object> comments = new HashSet<>();
}
