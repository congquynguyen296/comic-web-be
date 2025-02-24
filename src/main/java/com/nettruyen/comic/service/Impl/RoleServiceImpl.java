package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.constant.RoleEnum;
import com.nettruyen.comic.dto.request.authentication.RoleCreationRequest;
import com.nettruyen.comic.entity.RoleEntity;
import com.nettruyen.comic.exception.AppException;
import com.nettruyen.comic.exception.ErrorCode;
import com.nettruyen.comic.repository.internal.IRoleRepository;
import com.nettruyen.comic.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity
public class RoleServiceImpl implements IRoleService {

    IRoleRepository roleRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RoleEnum createRole(RoleCreationRequest request) {

        if (roleRepository.findByRoleName(request.getRoleName()).isPresent())
            throw new AppException(ErrorCode.ROLE_EXISTED);

        try {

            RoleEntity newRole = RoleEntity.builder()
                    .roleName(request.getRoleName())
                    .users(new HashSet<>())
                    .build() ;

            return roleRepository.save(newRole).getRoleName();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public RoleEnum updateRole(RoleEnum role) {
        return null;
    }
}
