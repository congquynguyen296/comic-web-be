package com.nettruyen.comic.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nettruyen.comic.constant.RoleEnum;
import com.nettruyen.comic.entity.RoleEntity;
import com.nettruyen.comic.entity.UserEntity;
import com.nettruyen.comic.repository.internal.IRoleRepository;
import com.nettruyen.comic.repository.internal.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(IUserRepository userRepository, IRoleRepository roleRepository) {

        return args -> {
            if(userRepository.findByUsername("admin") == null) {

                RoleEntity adminRole = roleRepository.findByRoleName(RoleEnum.ADMIN)
                        .orElseGet(() -> roleRepository.save(RoleEntity.builder()
                                .roleName(RoleEnum.ADMIN)
                                .build()));

                Set<RoleEntity> roles = new HashSet<>();
                roles.add(adminRole);

                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .firstName("Huỳnh")
                        .lastName("Mỹ Huyền")
                        .isActive(1)
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(admin);
                log.warn("ADMIN has been created with default password is 'admin', please change it!");
            }
        };
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // yyyy-MM-dd
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
