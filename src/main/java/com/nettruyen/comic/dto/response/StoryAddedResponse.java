package com.nettruyen.comic.dto.response;

import com.nettruyen.comic.constant.StatusEnum;
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
public class StoryAddedResponse {
    String title;

    String author;

    String description;

    StatusEnum status;

    String coverImage;

    Date createdAt;

    Date updatedAt;

    Set<String> generates = new HashSet<>();
}
