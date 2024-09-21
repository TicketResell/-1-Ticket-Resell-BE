//package com.teamseven.ticketresell.api;
//
//import com.teamseven.ticketresell.dto.AccountDTO;
//import com.teamseven.ticketresell.entity.UserEntity;
//import com.teamseven.ticketresell.service.impl.AccountService;
//import com.teamseven.ticketresell.converter.AccountConverter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/api/accounts")
//public class GoogleLoginAPI {
//
//    private static final Logger logger = LoggerFactory.getLogger(GoogleLoginAPI.class);
//
//    @Autowired
//    private AccountService accountService;
//
//    @GetMapping("/login-google")
//    public String googleLogin() {
//        logger.info("Accessing Google login page");
//
//        return "login-googlepage"; // Đảm bảo trang JSP tồn tại
//    }
//
//    @GetMapping("/login-success")
//    public String getGoogleAccountInfo(@AuthenticationPrincipal OAuth2AuthenticationToken authentication, Model model) {
//        logger.info("Handling Google login success");
//
//        if (authentication == null) {
//            logger.warn("Authentication is null");
//            model.addAttribute("message", "Authentication failed");
//            return "error-page"; // Bạn có thể tạo trang JSP báo lỗi
//        }
//
//        OAuth2User principal = authentication.getPrincipal();
//        String username = (String) principal.getAttribute("email");
//        String googleToken = authentication.getAuthorizedClientRegistrationId(); // Lấy token của Google
//
//        // Kiểm tra log token và email
//        logger.info("Authenticated user email: {}", username);
//        logger.info("Google token: {}", googleToken);
//
//        // Tiếp tục xử lý DTO
//        UserEntity existingUser = accountService.findByUsername(username);
//        if (existingUser == null) {
//            logger.info("User not found, creating a new account with username: {}", username);
//            String defaultPassword = "google";
//            existingUser = accountService.createNewAccount(username, defaultPassword);
//        }
//
//        AccountConverter converter = new AccountConverter();
//        AccountDTO accountDTO = converter.toDTO(existingUser);
//
//        // Log DTO
//        logger.info("AccountDTO: {}", accountDTO);
//
//        // Thêm thông tin vào model
//        model.addAttribute("username", username);
//        model.addAttribute("token", googleToken);
//
//        return "login-success"; // Trả về trang login-success.jsp
//    }
//
//}
