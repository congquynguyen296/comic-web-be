package com.nettruyen.comic.dto.request.authentication;

import com.nettruyen.comic.constant.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleCreationRequest {
    RoleEnum roleName;
}
