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
    private String googleClientId = "310764216947-glor2ci0tha7scaf77cgmiqrod6c58fq.apps.googleusercontent.com";

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
        // Lấy giá trị từ request body
        String identifier = loginRequest.get("identifier");
        String password = loginRequest.get( "password");

        // In thông tin ra để kiểm tra
        System.out.println("Identifier: " + identifier);
        System.out.println("Password: " + password);

        // Xác thực người dùng dựa trên email hoặc username và mật khẩu
        UserDTO result = userService.login(identifier, password);
        String exceptionMessage = "";

        // Kiểm tra trạng thái người dùng
        if (result != null) {
            System.out.println("User status: " + result.getStatus());
            System.out.println("Is Verified: " + result.isVerifiedEmail());

            if (result.getStatus().equals("banned")) {
                exceptionMessage = "Your account has been banned.";
            } else if (!result.isVerifiedEmail()) {
                exceptionMessage = "You are not verified.";
            }

            if (!exceptionMessage.isBlank()) {
                System.out.println(exceptionMessage);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionMessage);
            }

            // Tạo JWT nếu thông tin hợp lệ
            String jwt = jwtUtil.generateToken(result.getId(),result.getUsername(), result.getEmail(), result.getFullname(), result.getUserImage(), result.getRole());
            System.out.println("DTO: " + result);
            return ResponseEntity.ok(new JwtResponse(jwt));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email hoặc mật khẩu không hợp lệ");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        System.out.println("REQUEST DTO:" + userDTO);

        // Chuyển đổi DTO thành Entity và lưu user
        UserEntity newUser = accountConverter.toEntity(userDTO);
        newUser.setCreatedDate(LocalDateTime.now());
        newUser.setRole("user"); //default == user.
        newUser.setStatus("active");


        newUser.setCreatedDate(LocalDateTime.now());
        userDTO.setVerifiedEmail(false);
        UserEntity savedUser = userRepository.save(newUser);
        System.out.println("SAVED USER:"+savedUser);


        // Gửi email xác thực
        String verificationLink = "http://localhost:8084/api/accounts/verify?email=" + userDTO.getEmail();
        String subject = "Email Verification";
        String body = "Thank you for registering. Please click the link below to verify your email:\n" + verificationLink;

        emailService.sendEmail(userDTO.getEmail(), subject, body);

        return ResponseEntity.ok("Registration successful. Please check your email to verify your account.");
    }
    // create account
    @PostMapping("/staff")
    public ResponseEntity<?> createAccount(@RequestBody UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        System.out.println("REQUEST DTO:" + userDTO);

        // Chuyển đổi DTO thành Entity và lưu user
        UserEntity newUser = accountConverter.toEntity(userDTO);
        newUser.setCreatedDate(LocalDateTime.now());
//        newUser.setRole("user"); //default == user.
        newUser.setStatus("active");

        newUser.setCreatedDate(LocalDateTime.now());
        userDTO.setVerifiedEmail(true);
        UserEntity savedUser = userRepository.save(newUser);
        System.out.println("SAVED USER:"+savedUser);

        return ResponseEntity.ok("Create account successful");
    }
    // get all accounts
    @GetMapping("/staff")
    public ResponseEntity<?> getAccounts() {
        List<UserEntity> accounts = userRepository.findAll();
        return ResponseEntity.ok(accounts.stream().map(accountConverter::toDTO).toList());
    }
    // update account
    @PutMapping("/staff/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        // Kiểm tra xem user có tồn tại không
        UserEntity existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Cập nhật thông tin người dùng
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setFullname(userDTO.getFullname());
        existingUser.setUserImage(userDTO.getUserImage());
        existingUser.setStatus(userDTO.getStatus());
        existingUser.setVerifiedEmail(userDTO.isVerifiedEmail());
        existingUser.setRole(userDTO.getRole());

        // Lưu lại user đã cập nhật
        UserEntity updatedUser = userService.save(existingUser);

        return ResponseEntity.ok("User updated successfully");
    }
    // delete account by id
    @DeleteMapping("/staff/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        // Kiểm tra xem user có tồn tại không
        UserEntity existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Xóa tài khoản người dùng
        userService.delete(id);

        return ResponseEntity.ok("User deleted successfully");
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

        // Xử lý logic gửi email xác thực
        String verificationLink = "http://localhost:8084/api/accounts/reset?email=" + email;
        String subject = "Reset Password";
        String body = "Please click the link below to reset your password:\n" + verificationLink;

        emailService.sendEmail(email, subject, body);

        return ResponseEntity.ok("Reset password link has been sent to your email!");
    }
    // update new password
    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        // Kiểm tra email có tồn tại trong database không
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email!");
        }

        // Cập nhật mật khẩu mới=
        user.setPassword(newPassword);
        userRepository.save(user);  // Lưu user với mật khẩu mới

        return ResponseEntity.ok("Password has been updated successfully!");
    }


    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("email") String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        // Cập nhật trạng thái xác thực email
        user.setVerifiedEmail(true);
        userRepository.save(user);

        return ResponseEntity.ok("Email verified successfully.");
    }


    @GetMapping("/profile/{username}")
    public ResponseEntity<?> viewProfile(@PathVariable String username) {
        // Lấy đối tượng Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        // Lấy tên người dùng hiện tại từ Authentication
        String currentUser = authentication.getName();

        // Tìm người dùng theo username
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Lấy vai trò từ username
        String role = userService.getUserRoleByUsername(user.getUsername());

        // Kiểm tra quyền truy cập dựa trên vai trò và tên người dùng hiện tại
        if (role.equals("ADMIN") || role.equals("STAFF") || currentUser.equals(user.getUsername())) {
            // Trả về thông tin người dùng nếu đủ quyền
            return ResponseEntity.ok(accountConverter.toDTO(user));
        }

        // Nếu không đủ quyền, trả về lỗi 403
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to view this profile");
    }



    // Modify Profile
    @PutMapping("/profile/{username}")
    public ResponseEntity<?> editProfile(@PathVariable String username, @RequestBody UserDTO userDTO, @RequestParam boolean isAdmin) {
        // Find username
        UserEntity existingUser = userRepository.findByUsername(username);

        if (existingUser != null) {
            // Convert DTO -> Entity
            UserEntity updatedUser = accountConverter.toEntity(userDTO, existingUser, isAdmin);

            // Save to db
            userRepository.save(updatedUser);

            // ìf everything runs smoothly, return DTO
            return ResponseEntity.ok(accountConverter.toDTO(updatedUser));
        }

        // if user not exist, return 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }


    // Login with Google Oauth
    @PostMapping("/login-google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) {
        String idTokenString = body.get("id_token");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");


                // Check if user exists
                UserEntity user = userRepository.findByEmail(email);
                if (user == null) {
                    // Create a new user if not exists
                    user = new UserEntity();
                    user.setUsername(email + "_google"); // Đảm bảo username không bị trùng
                    user.setPassword(email + "_password"); // Đặt tạm mật khẩu
                    user.setEmail(email);
                    user.setStatus("active");
                    user.setRole("user"); //default == user.
                    user.setCreatedDate(LocalDateTime.now());
                    user.setVerifiedEmail(true);
                    user = userRepository.save(user);
                }

                // Generate JWT token
                String jwt = jwtUtil.generateToken(user.getId(),user.getUsername(),user.getEmail(), user.getFullname(), user.getUserImage(), user.getRole());

                // Return user info and JWT
                return ResponseEntity.ok(new JwtResponse(jwt));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
