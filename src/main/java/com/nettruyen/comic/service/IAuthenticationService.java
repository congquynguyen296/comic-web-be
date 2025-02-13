package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.authentication.ActiveAccountRequest;
import com.nettruyen.comic.dto.request.authentication.IntrospectRequest;
import com.nettruyen.comic.dto.request.authentication.LoginRequest;
import com.nettruyen.comic.dto.request.authentication.RegisterRequest;
import com.nettruyen.comic.dto.response.authentication.AuthenticationResponse;
import com.nettruyen.comic.dto.response.authentication.IntrospectResponse;
import com.nettruyen.comic.dto.response.authentication.ResendOtpResponse;
import com.nettruyen.comic.dto.response.UserResponse;

public interface IAuthenticationService {

    AuthenticationResponse login(LoginRequest request);

    String logout(String username);

    ResendOtpResponse resendOtp(ActiveAccountRequest request);

    UserResponse activeAccount(ActiveAccountRequest request);

    UserResponse register(RegisterRequest request);

    IntrospectResponse introspect(IntrospectRequest request);
}
