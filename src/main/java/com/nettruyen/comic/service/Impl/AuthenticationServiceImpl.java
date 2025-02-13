package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.constant.RoleEnum;
import com.nettruyen.comic.dto.request.authentication.ActiveAccountRequest;
import com.nettruyen.comic.dto.request.authentication.IntrospectRequest;
import com.nettruyen.comic.dto.request.authentication.LoginRequest;
import com.nettruyen.comic.dto.request.authentication.RegisterRequest;
import com.nettruyen.comic.dto.response.authentication.AuthenticationResponse;
import com.nettruyen.comic.dto.response.authentication.IntrospectResponse;
import com.nettruyen.comic.dto.response.authentication.ResendOtpResponse;
import com.nettruyen.comic.dto.response.UserResponse;
import com.nettruyen.comic.entity.RoleEntity;
import com.nettruyen.comic.entity.UserEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.UserMapper;
import com.nettruyen.comic.repository.IRoleRepository;
import com.nettruyen.comic.repository.IUserRepository;
import com.nettruyen.comic.service.IAuthenticationService;
import com.nettruyen.comic.service.IAccountService;
import com.nettruyen.comic.util.OtpGenerator;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
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
    IRoleRepository roleRepository;


    static final long VALID_DURATION = 3600;    // Thgian hợp lệ của 1 token
    static final long REFRESHABLE_DURATION = 3600;  // Thgian làm mới của 1 token

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        log.info("SignerKey: {}", SIGNER_KEY);

        UserEntity user = userRepository.findByUsername(request.getUsername());
        if (user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword())
                && user.getIsActive() == 1;
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Generate token when authenticate success
        String token = generateToken(user);

        // Tạo một authentication thủ công, do Spring Security không tự động cập nhật SecurityContextHolder
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

        // Gán cái vừa tạo cho SecurityContextHolder biết
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Show info of user (contain in context of security context holder)
        var contextAuthentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", contextAuthentication.getName());
        contextAuthentication.getAuthorities().forEach(grantedAuthority -> {
            log.info("GrantedAuthority: {}", grantedAuthority);
        });

        return AuthenticationResponse.builder()
                .isActive((user.getIsActive() == null || user.getIsActive() == 0) ? 0 : 1)
                .token(token)
                .message("Login successful with " + user.getUsername())
                .build();
    }


    @Override   // Chưa làm
    public String logout(String username) {

        // Logic cho logout sẽ ở đây (token - khi mở rộng hệ thống)

        UserEntity user = userRepository.findByUsername(username);
        if (user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        return "Logout successful with " + user.getUsername();
    }

    @Override   // Chưa làm
    public ResendOtpResponse resendOtp(ActiveAccountRequest request) {

        // Check user còn tồn tại không
        UserEntity user = userRepository.findByEmail(request.getEmail());
        if (user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        // Check đã active chưa
        if (user.getIsActive() == null || user.getIsActive() == 0) {
            try {
                // Tạo OTP mới và lưu vào Redis
                String newOtpCode = OtpGenerator.generateOtp();
                accountService.saveOtp(user.getEmail(), newOtpCode);

                // Gửi email OTP mới
                String subject = "🔑 Resend OTP for Your Q.comic Account";
                String body = "Hello " + user.getUsername() + ",\n\n"
                        + "We noticed that you requested a new OTP to activate your account. Please find your new OTP code below:\n\n"
                        + "🔒 Your OTP Code: " + newOtpCode + "\n\n"
                        + "This code is valid for the next 10 minutes. Please do not share this code with anyone.\n\n"
                        + "If you did not request this, please ignore this email.\n\n"
                        + "Best regards,\n"
                        + "The Q.comic Team";
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
    public UserResponse activeAccount(ActiveAccountRequest request) {

        boolean isValidOtp = accountService.validateOtp(request.getEmail(), request.getOtpCode());
        if (!isValidOtp)
            throw new AppException(ErrorCode.INVALID_OTP_OR_EXPIRED);

        try {

            UserEntity user = userRepository.findByEmail(request.getEmail());
            if (user == null)
                throw new AppException(ErrorCode.USER_NOT_EXISTED);

            user.setIsActive(1);
            UserEntity saveUser = userRepository.save(user);

            var result = userMapper.toUserResponse(saveUser);
            result.setRoles(saveUser.getRoles().stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.toSet()));

            return result;

        } catch (Exception e) {
            log.error("Error while validating OTP for user: {}", request.getEmail(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()) != null) {
            log.info("Email existed");
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (userRepository.findByUsername(request.getUsername()) != null) {
            log.info("Username existed");
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Build new user for this request
        UserEntity userEntity = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(0)
                .firstName("New")
                .lastName("User")
                .dob(LocalDate.now())
                .build();
        Set<RoleEntity> roles = new HashSet<>();
        roleRepository.findByRoleName(RoleEnum.USER).ifPresent(roles::add);
        userEntity.setRoles(roles);

        try {
            var newUser = userRepository.save(userEntity);

            // Tạo otp + token và lưu vào redis
            String otpCode = OtpGenerator.generateOtp();
            String token = UUID.randomUUID().toString();
            accountService.saveOtp(newUser.getEmail(), otpCode);

            // Send otp qua email
            String subject = "🔑 Activate Your Account at Q.comic!";
            String body = "Hello " + newUser.getUsername() + ",\n\n"
                    + "Thank you for signing up at Q.comic. To activate your account, please use the following OTP code:\n\n"
                    + "🔒 Your OTP Code: " + otpCode + "\n\n"
                    + "This code is valid for the next 10 minutes. Please do not share this code with anyone.\n\n"
                    + "If you did not request this, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "The Q.comic Team";
            accountService.sendEmail(newUser.getEmail(), subject, body);

            UserResponse result = userMapper.toUserResponse(newUser);

            // Map roles
            result.setRoles(newUser.getRoles().stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.toSet()));

            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {

        var token = request.getToken();
        boolean isValid = true;

        try {
            var signed = verifyToken(token, false);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
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
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(userEntity.getRoles()))
            userEntity.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getRoleName().name());
            });

        return stringJoiner.toString();
    }

    // Verify token
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.INVALID_EXPIRED_TOKEN);

        // Thực hiện logic lưu token tại đây

        return signedJWT;
    }
}
