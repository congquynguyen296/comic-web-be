package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.ActiveAccountRequest;
import com.nettruyen.comic.dto.request.LoginRequest;
import com.nettruyen.comic.dto.request.RegisterRequest;
import com.nettruyen.comic.dto.request.UserCreationRequest;
import com.nettruyen.comic.dto.response.AuthenticationResponse;
import com.nettruyen.comic.dto.response.ResendOtpResponse;
import com.nettruyen.comic.dto.response.UserResponse;

public interface IAuthenticationService {

    AuthenticationResponse login(LoginRequest request);

    String logout(String username);

    ResendOtpResponse resendOtp(ActiveAccountRequest request);

    UserResponse activeAccount(ActiveAccountRequest request);

    UserResponse register(RegisterRequest request);
}
