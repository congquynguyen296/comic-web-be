package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.authentication.*;
import com.nettruyen.comic.dto.response.authentication.AuthenticationResponse;
import com.nettruyen.comic.dto.response.authentication.IntrospectResponse;
import com.nettruyen.comic.dto.response.authentication.ResendOtpResponse;
import com.nettruyen.comic.dto.response.UserResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {

    AuthenticationResponse login(LoginRequest request);

    String logout(LogoutRequest request) throws ParseException, JOSEException;

    ResendOtpResponse resendOtp(ActiveAccountRequest request);

    UserResponse activeAccount(ActiveAccountRequest request);

    UserResponse register(RegisterRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
