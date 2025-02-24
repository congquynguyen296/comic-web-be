package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.constant.RoleEnum;
import com.nettruyen.comic.dto.request.authentication.UserCreationRequest;
import com.nettruyen.comic.dto.response.UserResponse;
import com.nettruyen.comic.entity.RoleEntity;
import com.nettruyen.comic.entity.UserEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.mapper.UserMapper;
import com.nettruyen.comic.repository.IRoleRepository;
import com.nettruyen.comic.repository.IUserRepository;
import com.nettruyen.comic.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity
public class UserServiceImpl implements IUserService {

    UserMapper userMapper;
    IUserRepository userRepository;
    IRoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserResponse createUser(UserCreationRequest request) {

        if (userRepository.findByUsername(request.getUsername()) != null)
            throw new AppException(ErrorCode.USER_EXISTED);

        // Map các thông tin cần thiết
        UserEntity userEntity = userMapper.toUserEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<RoleEntity> roles = new HashSet<>();
        roleRepository.findByRoleName(RoleEnum.USER).ifPresent(roles::add);
        userEntity.setRoles(roles);

        try {
            var newUser = userRepository.save(userEntity);

            // Map lại set role
            var result = userMapper.toUserResponse(newUser);
            result.setRoles(newUser.getRoles().stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.toSet()));
            return result;

        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public UserResponse updateUser(Object request) {
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(String username) {

        if (username.isEmpty() || username == null)
            throw new AppException(ErrorCode.UNCATEGORIZED);

        try {
            var userExisted = userRepository.findByUsername(username);
            if (userExisted == null) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            } else {
                userExisted.setIsActive(0);
                userRepository.save(userExisted);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponse findUserByUsername(String username) {

        if (username.isEmpty() || username == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        try {
            UserEntity user = userRepository.findByUsername(username);

            UserResponse userResponse = userMapper.toUserResponse(user);
            userResponse.setRoles(user.getRoles().stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.toSet()));

            return userResponse;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // @PreAuthorize("hasAuthority('APPROVE_POST')")    // Áp dụng cho permission
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> findAllUsers() {

        try {
            List<UserEntity> users = userRepository.findAll();

            if (!users.isEmpty()) {
                return users.stream().map(user -> {
                    UserResponse userResponse = userMapper.toUserResponse(user);

                    userResponse.setRoles(user.getRoles().stream()
                            .map(RoleEntity::getRoleName)
                            .collect(Collectors.toSet()));

                    return userResponse;
                }).collect(Collectors.toList());

            } else {
                log.error("User list is empty");
            }

        } catch (AppException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")  // Chỉ lấy được thông tin của bản thân
    public UserResponse findUserById(String id) {

        if (id == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        try {
            UserEntity userExisted = userRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            UserResponse userResponse = userMapper.toUserResponse(userExisted);

            // Map role
            Set<RoleEnum> roles = userExisted.getRoles().stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.toSet());
            userResponse.setRoles(roles);
            return userResponse;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    // Khi đăng nhập ròi thì thông tin sẽ được giữ trong context
    @Override
    public UserResponse getMyInfo() {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        try {
            UserEntity user = userRepository.findByUsername(username);
            if (user == null)
                throw new AppException(ErrorCode.USER_NOT_EXISTED);

            UserResponse userResponse = userMapper.toUserResponse(user);
            Set<RoleEnum> roles = user.getRoles().stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.toSet());
            userResponse.setRoles(roles);

            return userResponse;

        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }
}
