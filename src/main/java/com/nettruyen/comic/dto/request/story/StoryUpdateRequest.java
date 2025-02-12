package com.nettruyen.comic.dto.request.story;

import com.nettruyen.comic.constant.StatusEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StoryUpdateRequest {

    String id;

    String title;

    String author;

    String description;

    StatusEnum status;

    String coverImage;

    Set<String> generates = new HashSet<>();

}
