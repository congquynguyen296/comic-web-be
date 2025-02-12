package com.nettruyen.comic.dto.request.generate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GenerateUpdateRequest {

    String id;

    String name;

    String description;
}
