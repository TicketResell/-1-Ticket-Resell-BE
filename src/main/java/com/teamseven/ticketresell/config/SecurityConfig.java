package com.teamseven.ticketresell.config;

import com.teamseven.ticketresell.filter.JwtRequestFilter;
import com.teamseven.ticketresell.service.impl.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF vì đang sử dụng API
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.setAllowedOrigins(List.of("http://localhost:3000","http://ticketresell-swp.click","http://react.ticketresell-swp.click")); // Địa chỉ frontend
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Cho phép OPTIONS không cần xác thực
                        .requestMatchers(
                                // Public API
                                "/api/accounts/login",
                                "/api/accounts/agency/{userId}",
                                "/api/accounts/register",
                                "/api/accounts/verify",
                                "/api/accounts/reset",
                                "/api/accounts/update-password",
                                "/api/accounts/login-google",
                                "/api/tickets",
                                "/api/tickets/saleprice-desc",
                                "/api/tickets/saleprice-asc",
                                "/api/tickets/search-date",
                                "/api/tickets/category-search",
                                "/api/tickets/upcoming",
                                "/api/tickets/upcoming/{date}",
                                "/api/tickets/search/{query}",
                                "/api/categories",
                                "/api/categories/{id}",
                                "/health",
                                "/sendMessage",
                                "/chat/**",
                                "/ws/**",
                                "/chat-websocket/**",
                                "/api/orders",
                                "/api/orders/{orderId}",
                                "/api/orders/create",
                                "/api/orders/buyer/{buyerId}",
                                "/api/orders/seller/{sellerId}",
                                "/api/orders/update-payment-status/{orderId}",
                                "/api/orders/update-order-status/{orderId}",
                                "/api/orders/update-order-status-refund/{orderId}",
                                "/api/vnpay/create-payment",
                                "/api/vnpay/callback",
                                "/api/ratings",
                                "/api/ratings/create-report",
                                "/api/ratings/seller/{sellerId}",
                                "/api/ratings/{rateId}",
                                "api/accounts/is-full-data/{id}",
                                "api/accounts/profile/{username}"
                        ).permitAll() // Cho phép các endpoint không cần xác thực
                        .anyRequest().authenticated() // Yêu cầu xác thực cho các yêu cầu khác
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            // Quản lý ngoại lệ khi không xác thực
                            logger.error("Unauthorized error: {}", authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                );

        // Thêm filter JWT vào chuỗi bảo mật
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
