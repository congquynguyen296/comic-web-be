package com.nettruyen.comic.dto.request.generate;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GenerateAddedRequest {

    String name;

    String description;

    Set<String> stories = new HashSet<>();
}
