package com.nettruyen.comic.dto.request.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RegisterRequest {

    String username;

    String password;

    String confirmPassword;

    String email;

    boolean termsAccepted;
}
