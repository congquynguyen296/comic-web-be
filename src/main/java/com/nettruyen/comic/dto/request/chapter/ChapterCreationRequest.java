package com.nettruyen.comic.dto.request.chapter;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChapterCreationRequest {

    String url;

    String title;

    String content;

    Integer chapterNumber;

    String storyId;
}
