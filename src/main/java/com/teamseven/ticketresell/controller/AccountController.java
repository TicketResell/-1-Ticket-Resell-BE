package com.teamseven.ticketresell.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.teamseven.ticketresell.converter.UserConverter;
import com.teamseven.ticketresell.dto.UserDTO;
import com.teamseven.ticketresell.dto.JwtResponse;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.EmailService;
import com.teamseven.ticketresell.service.impl.UserService;
import com.teamseven.ticketresell.service.impl.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.teamseven.ticketresell.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter accountConverter;

    @Autowired
    private SmsService smsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String identifier = loginRequest.get("identifier");
        String password = loginRequest.get("password");

        try {
            UserDTO userDTO = userService.login(identifier, password);

            // Tạo JWT nếu thông tin hợp lệ
            String jwt = jwtUtil.generateToken(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getFullname(), userDTO.getUserImage(), userDTO.getRole());
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            UserDTO registeredUser = userService.register(userDTO);
            return ResponseEntity.ok("Registration successful. Please check your email to verify your account.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // create account
    @PostMapping("/staff")
    public ResponseEntity<?> createAccount(@RequestBody UserDTO userDTO) {
        try {
            UserEntity savedUser = userService.createNewAccount(userDTO.getUsername(),userDTO.getPassword());
            return ResponseEntity.ok("Create account successful");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // get all accounts
    @GetMapping("/staff")
    public ResponseEntity<?> getAccounts() {
        List<UserEntity> accounts = userRepository.findAll();
        return ResponseEntity.ok(accounts.stream().map(accountConverter::toDTO).toList());
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        try {
            UserEntity updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok("User updated successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    // reset password by mail
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");  // Trích xuất giá trị email từ JSON

        // Kiểm tra email đã tồn tại chưa
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email was not registered before!");
        }

        // gửi email xác thực
        String verificationLink = "http://localhost:8084/api/accounts/reset?email=" + email;
        String subject = "Reset Password";
        String body = "Please click the link below to reset your password:\n" + verificationLink;

        emailService.sendEmail(email, subject, body);

        return ResponseEntity.ok("Reset password link has been sent to your email!");
    }

    // update new password
    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String newPassword = request.get("newPassword");

            userService.updatePassword(email, newPassword);
            return ResponseEntity.ok("Password updated successfully!");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    // verify
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("email") String email) {
        try {
            userService.verifyEmail(email);
            return ResponseEntity.ok("Email verified successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> viewProfile(@PathVariable String username) {
        try {
            // Lấy đối tượng Authentication từ SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Kiểm tra xác thực
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            // Lấy tên người dùng hiện tại từ Authentication
            String currentUser = authentication.getName();

            UserDTO userDTO = userService.viewProfile(username, currentUser);
            return ResponseEntity.ok(userDTO);

        } catch (RuntimeException ex) {
            if (ex.getMessage().equals("User not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            } else { // "You are not allowed to view this profile"
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
            }
        }
    }


    // Modify Profile
    @PutMapping("/profile/{username}")
    public ResponseEntity<?> editProfile(@PathVariable String username,
                                         @RequestBody UserDTO userDTO,
                                         @RequestParam boolean isAdmin) {
        try {
            UserDTO updatedUserDTO = userService.editProfile(username, userDTO, isAdmin);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    // Login with Google Oauth
    @PostMapping("/login-google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) {
        String idTokenString = body.get("id_token");
        try {
            JwtResponse jwtResponse = userService.loginWithGoogle(idTokenString);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

}