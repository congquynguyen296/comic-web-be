package com.nettruyen.comic.dto.request.other;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentRequest {

    String content;

    String userId;

    String chapterId;

    String parentId;
}
