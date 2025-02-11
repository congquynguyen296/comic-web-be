package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.constant.RoleEnum;
import com.nettruyen.comic.dto.request.UserCreationRequest;
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
public class UserServiceImpl implements IUserService {

    UserMapper userMapper;
    IUserRepository userRepository;
    IRoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

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

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public UserResponse findUserByUsername(String username) {
        return null;
    }

    @Override
    public List<UserResponse> findAllUsers() {



        return List.of();
    }
}
