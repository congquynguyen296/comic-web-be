package com.nettruyen.comic.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Cho phép React domain
        corsConfiguration.addAllowedMethod("*"); // Cho phép tất cả các phương thức
        corsConfiguration.addAllowedHeader("*"); // Cho phép tất cả header
        corsConfiguration.setAllowCredentials(true); // Nếu cần cookie, đặt true

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
