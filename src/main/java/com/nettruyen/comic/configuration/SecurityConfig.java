package com.nettruyen.comic.configuration;

import com.nettruyen.comic.exception.CustomJwtAuthenticationEntryPoint;
import com.nettruyen.comic.exception.CustomJwtDecoder;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

    String[] PUBLIC_ENDPOINT = {
            "/api/auth/**",
            // "/api/users/**",
            "/api/story/**",
            "/api/generate/**",
            "/api/stories/**",
            "/api/role/**",

            "/api/admin/**",
    };

    // CustomJwtDecoder jwtDecoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HttpSession httpSession) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINT).permitAll()
                        // .requestMatchers("/api/admin/**").hasAuthority("SCOPE_ADMIN")
                        .anyRequest().authenticated()
                );
                // .exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler));

        // Cấu hình chấp nhận token ở header khi thực request đến
        http.oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwtConfigurer -> {

                // Config cho một IDP bên thứ 3: Nghĩa là giao cho bên này đảm nhiệm quá trình đăng nhập
                // jwtConfigurer.jwkSetUri("https://security-outbound");

                jwtConfigurer.decoder(jwtDecoder());

            }).authenticationEntryPoint(new CustomJwtAuthenticationEntryPoint());
        });

        http.csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec spec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(spec).macAlgorithm(MacAlgorithm.HS512).build();
    }
}
