package com.nettruyen.comic.dto.request.authentication;

import com.nettruyen.comic.validation.MinAge;
import com.nettruyen.comic.validation.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreationRequest {

    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    @NotEmpty(message = "First name is not empty")
    String firstName;

    @NotEmpty(message = "Last name is not empty")
    String lastName;

    @ValidEmail
    String email;

    @MinAge(value = 16, message = "You must be at least 16 years old to register")
    LocalDate dob;

    @NotNull(message = "User active status must not be null")
    Integer isActive;
}
