package com.teamseven.ticketresell.api;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.teamseven.ticketresell.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountAPI {
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
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        // Sử dụng email để xác thực
        System.out.println("Email: " + userDTO.getEmail());
        System.out.println("Password: " + userDTO.getPassword());

        UserDTO result = userService.login(userDTO.getEmail(), userDTO.getPassword());
        String exceptionMessage = "";

        // In ra trạng thái người dùng để kiểm tra
        if(result != null) {
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
            String jwt = jwtUtil.generateToken(result.getUsername(),result.getEmail(), result.getFullname(), result.getUserImage(), result.getRole());
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


    // View Profile
    @GetMapping("/profile/{username}")
    public ResponseEntity<?> viewProfile(@PathVariable String username) {
        UserEntity user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(accountConverter.toDTO(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    // Modify Profile
    @PutMapping("/profile/{username}")
    public ResponseEntity<?> editProfile(@PathVariable String username, @RequestBody UserDTO userDTO, @RequestParam boolean isAdmin) {
        // Find username
        UserEntity existingUser = userService.findByUsername(username);

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
                    user.setVerifiedEmail(true); // Giả định rằng email đã được xác thực qua Google
                    user = userRepository.save(user);
                }

                // Generate JWT token
                String jwt = jwtUtil.generateToken(user.getUsername(),user.getEmail(), user.getFullname(), user.getUserImage(), user.getRole());

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
