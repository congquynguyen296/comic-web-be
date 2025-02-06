package com.nettruyen.comic.dto.response;

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
public class GenerateAddedResponse {

    String name;

    String description;

    String code;

    Set<String> stories = new HashSet<>();

    Date createdAt;

    Date updatedAt;
}
