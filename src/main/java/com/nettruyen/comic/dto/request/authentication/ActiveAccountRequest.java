package com.nettruyen.comic.dto.request.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ActiveAccountRequest {
    String email;
    String otpCode;
    String token;
}
