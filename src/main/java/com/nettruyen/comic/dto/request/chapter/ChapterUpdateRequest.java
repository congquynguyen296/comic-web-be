package com.nettruyen.comic.dto.request.chapter;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChapterUpdateRequest {

    String id;

    String title;

    String chapterNumber;

    String content;
}
