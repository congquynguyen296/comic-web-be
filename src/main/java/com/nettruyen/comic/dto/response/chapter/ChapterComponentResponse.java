package com.nettruyen.comic.dto.response.chapter;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChapterComponentResponse {

    String title;

    String date;

    Integer chapterNumber;

    String storyId;
}
