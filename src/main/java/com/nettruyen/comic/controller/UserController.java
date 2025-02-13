package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.authentication.UserCreationRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.UserResponse;
import com.nettruyen.comic.service.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserController {

    IUserService userService;

    @PostMapping()
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    @GetMapping()
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .result(userService.findAllUsers())
                .build();
    }
}
