package com.nettruyen.comic.dto.response.chapter;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChapterComponentResponse {

    String code;

    String title;

    String date;

    String chapterNumber;

    String storyId;
}
