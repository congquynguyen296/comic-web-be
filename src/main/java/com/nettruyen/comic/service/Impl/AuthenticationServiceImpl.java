package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.dto.request.ActiveAccountRequest;
import com.nettruyen.comic.dto.request.LoginRequest;
import com.nettruyen.comic.dto.request.UserCreationRequest;
import com.nettruyen.comic.dto.response.AuthenticationResponse;
import com.nettruyen.comic.dto.response.ResendOtpResponse;
import com.nettruyen.comic.entity.RoleEntity;
import com.nettruyen.comic.entity.UserEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.UserMapper;
import com.nettruyen.comic.repository.IUserRepository;
import com.nettruyen.comic.service.IAuthenticationService;
import com.nettruyen.comic.service.IAccountService;
import com.nettruyen.comic.util.OtpGenerator;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements IAuthenticationService {

    UserMapper userMapper;
    IUserRepository userRepository;
    PasswordEncoder passwordEncoder;
    IAccountService accountService;


    static final long VALID_DURATION = 3600;    // Thgian hợp lệ của 1 token

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        log.info("SignerKey: {}", SIGNER_KEY);

        UserEntity user = userRepository.findByUsername(request.getUsername());
        if (user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Generate token when authenticate success
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .isActive((user.getIsActive() == null || user.getIsActive() == 0) ? 0 : 1)
                .token(token)
                .message("Login successful with " + user.getUsername())
                .build();
    }

    @Override
    public String logout(String username) {

        // Logic cho logout sẽ ở đây (token - khi mở rộng hệ thống)

        UserEntity user = userRepository.findByUsername(username);
        if (user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        return "Logout successful with " + user.getUsername();
    }

    @Override
    public ResendOtpResponse resendOtp(ActiveAccountRequest request) {

        // Check user còn tồn tại không
        UserEntity user = userRepository.findByEmail(request.getEmail());
        if (user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        // Check đã active chưa
        // user.setIsActive((user.getIsActive() == null) ? 0 : 1);
        if (user.getIsActive() == null || user.getIsActive() == 0) {
            try {
                // Tạo OTP mới và lưu vào Redis
                String newOtpCode = OtpGenerator.generateOtp();
                accountService.saveOtp(user.getEmail(), newOtpCode);

                // Gửi email OTP mới
                String subject = "🔑 Resend OTP for Your NetTruyen Account";
                String body = "Hello " + user.getUsername() + ",\n\n"
                        + "We noticed that you requested a new OTP to activate your account. Please find your new OTP code below:\n\n"
                        + "🔒 Your OTP Code: " + newOtpCode + "\n\n"
                        + "This code is valid for the next 10 minutes. Please do not share this code with anyone.\n\n"
                        + "If you did not request this, please ignore this email.\n\n"
                        + "Best regards,\n"
                        + "The NetTruyen Team";
                accountService.sendEmail(user.getEmail(), subject, body);

                log.info("Resend OTP successfully for user: {}", user.getEmail());
                return ResendOtpResponse.builder()
                        .email(request.getEmail())
                        .otpCode(newOtpCode)
                        .build();

            } catch (Exception e) {
                log.error("Error while resending OTP for user: {}", user.getEmail(), e);
                throw new AppException(ErrorCode.UNCATEGORIZED);
            }
        }
        return null;
    }

    @Override
    public String activeAccount(ActiveAccountRequest request) {

        boolean isValidOtp = accountService.validateOtp(request.getEmail(), request.getOtpCode());
        if (!isValidOtp)
            throw new AppException(ErrorCode.INVALID_OTP_OR_EXPIRED);

        UserEntity user = userRepository.findByEmail(request.getEmail());
        if (user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        user.setIsActive(1);
        userRepository.save(user);
        return "Account active successfully.";
    }

    @Override
    public String register(UserCreationRequest request) {

        UserEntity existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        UserEntity userEntity = userMapper.toUserEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setIsActive(0);

        try {
            var user = userRepository.save(userEntity);

            // Tạo otp và lưu vào redis
            String otpCode = OtpGenerator.generateOtp();
            accountService.saveOtp(user.getEmail(), otpCode);

            // Send otp qua email
            String subject = "🔑 Activate Your Account at NetTruyen!";
            String body = "Hello " + user.getUsername() + ",\n\n"
                    + "Thank you for signing up at NetTruyen.com. To activate your account, please use the following OTP code:\n\n"
                    + "🔒 Your OTP Code: " + otpCode + "\n\n"
                    + "This code is valid for the next 10 minutes. Please do not share this code with anyone.\n\n"
                    + "If you did not request this, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "The NetTruyen Team";
            accountService.sendEmail(user.getEmail(), subject, body);

            return "Account created successfully. Please check your email to activate your account.";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }


    /* === Authentication for JWT === */

    // Generate token
    private String generateToken(UserEntity userEntity) {

        // Header
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // Payload: Data contain in body - claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userEntity.getUsername())
                .issuer("Q.comic - Admin")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS)))
                .claim("scope", buildScope(userEntity))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);


        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    // Build scope for claim - scope in body of token
    private String buildScope(UserEntity userEntity) {
        return userEntity.getRoles().stream()
                .map(role -> role.getRoleName().name()) // Convert enum thành string
                .collect(Collectors.joining(","));
    }

    //
}
