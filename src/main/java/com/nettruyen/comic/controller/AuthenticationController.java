package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.dto.request.ActiveAccountRequest;
import com.nettruyen.comic.dto.request.LoginRequest;
import com.nettruyen.comic.dto.request.UserCreationRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.dto.response.AuthenticationResponse;
import com.nettruyen.comic.dto.response.ResendOtpResponse;
import com.nettruyen.comic.dto.response.UserResponse;
import com.nettruyen.comic.service.IAccountService;
import com.nettruyen.comic.service.IAuthenticationService;
import com.nettruyen.comic.service.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationController {

    IAuthenticationService authenticationService;

    @PostMapping("/register")
    ApiResponse<String> register(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<String>builder()
                .code(200)
                .result(authenticationService.register(request))
                .build();
    }

    @PostMapping("/active-account")
    ApiResponse<String> activeAccount(@RequestBody ActiveAccountRequest request) {
        return ApiResponse.<String>builder()
                .code(200)
                .result(authenticationService.activeAccount(request))
                .build();
    }


    @PostMapping("/resend-otp")
    ApiResponse<ResendOtpResponse> resendOtp(@RequestBody ActiveAccountRequest request) {
        return ApiResponse.<ResendOtpResponse>builder()
                .code(200)
                .result(authenticationService.resendOtp(request))
                .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .result(authenticationService.login(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestParam("username") String username) {
        return ApiResponse.<String>builder()
                .code(200)
                .result(authenticationService.logout(username))
                .build();
    }
}
