package com.nettruyen.comic.dto.response.story;

import com.nettruyen.comic.constant.StatusEnum;
import com.nettruyen.comic.dto.response.chapter.ChapterComponentResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StoryResponse {

    String id;

    String title;

    String code;

    String author;

    String description;

    StatusEnum status;

    String coverImage;

    Date createdAt;

    Date updatedAt;

    Set<String> generates = new HashSet<>();

    Set<ChapterComponentResponse> chapters = new HashSet<>();
}
