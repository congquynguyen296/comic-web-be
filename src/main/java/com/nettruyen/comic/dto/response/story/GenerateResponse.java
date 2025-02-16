package com.nettruyen.comic.dto.response.story;

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
public class GenerateResponse {

    String id;

    String name;

    String description;

    String code;

    Set<String> stories = new HashSet<>();

    Date createdAt;

    Date updatedAt;
}
