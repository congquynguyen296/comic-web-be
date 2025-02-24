package com.nettruyen.comic.dto.response.user;

import com.nettruyen.comic.constant.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    LocalDate dob;
    Integer isActive;
    String picture;
    Set<RoleEnum> roles;
}
