package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.dto.request.ActiveAccountRequest;
import com.nettruyen.comic.dto.request.LoginRequest;
import com.nettruyen.comic.dto.request.UserCreationRequest;
import com.nettruyen.comic.dto.response.AuthenticationResponse;
import com.nettruyen.comic.dto.response.ResendOtpResponse;
import com.nettruyen.comic.entity.UserEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.UserMapper;
import com.nettruyen.comic.repository.IUserRepository;
import com.nettruyen.comic.service.IAuthenticationService;
import com.nettruyen.comic.service.IAccountService;
import com.nettruyen.comic.util.OtpGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements IAuthenticationService {

    UserMapper userMapper;
    IUserRepository userRepository;
    PasswordEncoder passwordEncoder;
    IAccountService accountService;

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        UserEntity user = userRepository.findByUsername(request.getUsername());
        if (user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return AuthenticationResponse.builder()
                .isActive((user.getIsActive() == null || user.getIsActive() == 0) ? 0 : 1)
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
}
