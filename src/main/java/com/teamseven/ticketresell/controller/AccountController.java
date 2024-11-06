package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.converter.UserConverter;
import com.teamseven.ticketresell.dto.UserDTO;
import com.teamseven.ticketresell.dto.JwtResponse;
import com.teamseven.ticketresell.dto.UserOnlineDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.entity.NotificationEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.NotificationRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.EmailService;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.teamseven.ticketresell.util.JwtUtil;

import java.time.LocalDateTime;
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
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String identifier = loginRequest.get("identifier");
        String password = loginRequest.get("password");

        try {
            UserDTO userDTO = userService.login(identifier, password);

            // Tạo JWT nếu thông tin hợp lệ
            String jwt = jwtUtil.generateToken(userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getFullname(), userDTO.getUserImage(), userDTO.getRole());
            System.out.println("JWT:" + jwt);
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
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) {
        try {
            userService.verifyEmail(email);
            // Tạo biến string để chứa HTML
            String htmlResponse = String.format(
                    "<!DOCTYPE html>\n"
                            + "<html lang=\"en\">\n"
                            + "<head>\n"
                            + "    <meta charset=\"UTF-8\">\n"
                            + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                            + "    <title>Email Verified</title>\n"
                            + "    <style>\n"
                            + "        body {\n"
                            + "            font-family: Arial, sans-serif;\n"
                            + "            background-color: #f4f6f9;\n"
                            + "            margin: 0;\n"
                            + "            padding: 0;\n"
                            + "            display: flex;\n"
                            + "            justify-content: center;\n"
                            + "            align-items: center;\n"
                            + "            height: 100vh;\n"
                            + "        }\n"
                            + "        .container {\n"
                            + "            background-color: #fff;\n"
                            + "            border-radius: 10px;\n"
                            + "            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\n"
                            + "            padding: 20px 40px;\n"
                            + "            text-align: center;\n"
                            + "        }\n"
                            + "        h1 {\n"
                            + "            color: #4CAF50;\n"
                            + "            font-size: 24px;\n"
                            + "        }\n"
                            + "        p {\n"
                            + "            font-size: 18px;\n"
                            + "            color: #555;\n"
                            + "        }\n"
                            + "        a {\n"
                            + "            text-decoration: none;\n"
                            + "            color: #fff;\n"
                            + "            background-color: #4CAF50;\n"
                            + "            padding: 10px 20px;\n"
                            + "            border-radius: 5px;\n"
                            + "            display: inline-block;\n"
                            + "            margin-top: 20px;\n"
                            + "        }\n"
                            + "        a:hover {\n"
                            + "            background-color: #45a049;\n"
                            + "        }\n"
                            + "    </style>\n"
                            + "</head>\n"
                            + "<body>\n"
                            + "    <div class=\"container\">\n"
                            + "        <h1>Email Verified Successfully!</h1>\n"
                            + "        <p><strong>%s</strong> has been successfully verified. You can now log in to your account.</p>\n"
                            + "        <a href=\"/login\">Go to Login</a>\n"
                            + "    </div>\n"
                            + "</body>\n"
                            + "</html>", email);
            return ResponseEntity.ok().body(htmlResponse);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
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

            String role = userService.getUserRoleByUsername(currentUser);

            UserDTO userDTO;

            if(!role.toLowerCase().equals("user")) {
                userDTO = userService.viewProfile(username, currentUser);
            } else {;
                userDTO = userConverter.toDTO(userRepository.findByUsername(currentUser));
            }
            System.out.println(userRepository.findByUsername(currentUser).getUserImage());
            System.out.println(userDTO);
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
                                         @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUserDTO = userService.editProfile(username, userDTO);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    // Update Agency
    @PutMapping("/agency/{userId}")
    public ResponseEntity<?> editAgency(@PathVariable Long userId) {
        try {
            UserEntity updatedUser = userService.setUserAgency(userId);
            // Tạo thông báo chúc mừng người dùng
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(updatedUser); // Đặt người nhận thông báo là user vừa được set agency
            notification.setTitle("Congratulations on Becoming an Agency!");
            notification.setMessage("Congratulations, " + updatedUser.getUsername() + "! You have been designated as an agency in our system. Take advantage of this new role to increase your transactions and benefits!");
            notification.setCreatedDate(LocalDateTime.now());
            // Lưu thông báo vào cơ sở dữ liệu
            notificationRepository.save(notification);
            return ResponseEntity.ok("User ID " + userId + " is now set as agency.");
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
    @PostMapping("/hidden-search-profile/{id}")
    public ResponseEntity<String> getUserNameByID(@PathVariable Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user.getFullname());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/is-full-data/{id}")
    public ResponseEntity<Boolean> isFullData(@PathVariable("id") String id) {
        try {
            Long longId = Long.parseLong(id);
            return ResponseEntity.ok(userService.isFullData(longId));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/get-avatar/{id}")
    public ResponseEntity<String> getAvatar(@PathVariable("id") String id) {
        try {
            Long longId = Long.parseLong(id);
            return ResponseEntity.ok(userService.getAvatarByID(longId));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/get-user-online-status/{id}")
    public ResponseEntity<?> getOnlineStatus(@PathVariable("id") Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        UserOnlineDTO onlineDTO = new UserOnlineDTO(user.isOnline(), user.getLastSeen());
        return ResponseEntity.ok(onlineDTO);
    }
    @PostMapping("/set-user-online/{status}/{id}")
    public boolean setUserOnline(@PathVariable String status, @PathVariable Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if(status.equals("online")) user.setOnline(true);
        else if(status.equals("offline")) user.setOnline(false);
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);
        userRepository.flush();
        return true;
    }


    //phục vụ môn SWT301
    @GetMapping("/extract-data-from-token")
    public ResponseEntity<UserEntity> extractNameFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUsername(authentication.getName());

        return ResponseEntity.ok(user);
    }

}