package com.nettruyen.comic.dto.request.wrapper;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ScraperChapterRequest {

    String url;
}
