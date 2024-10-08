package com.teamseven.ticketresell.config;

import com.teamseven.ticketresell.service.impl.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF vì bạn có thể đang sử dụng API
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.setAllowedOrigins(List.of("http://localhost:3000")); // Địa chỉ frontend
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Cho phép OPTIONS không cần xác thực
                        .requestMatchers(
                                "/api/accounts/login",
                                "/api/accounts/register",
                                "/api/accounts/verify",
                                "/send-test-sms-test",
                                "/api/accounts/login-google",
                                "/api/accounts/test-request-token",
                                "/api/tickets/create",
                                "/api/tickets",
                                "/api/tickets/{id}",
                                "/api/categories",
                                "/api/categories/{id}"
                        ).permitAll() // Cho phép các endpoint không cần xác thực
                        .anyRequest().authenticated() // Yêu cầu xác thực cho các yêu cầu khác
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            // Quản lý ngoại lệ khi không xác thực
                            logger.error("Unauthorized error: {}", authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login") // Cấu hình trang đăng nhập cho OAuth2
//                        .defaultSuccessUrl("/home", true) // Điều hướng khi đăng nhập thành công
//                        .failureUrl("/login?error=true") // Điều hướng khi đăng nhập thất bại
//                );
                .oauth2Login(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
