package com.teamseven.ticketresell.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.teamseven.ticketresell.dto.AccountDTO;
import com.teamseven.ticketresell.dto.JwtResponse;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.AccountService;
import com.teamseven.ticketresell.converter.AccountConverter;
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
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountConverter accountConverter;

    @Autowired
    private SmsService smsService;

    @Autowired
    private JwtUtil jwtUtil;

    // Đăng nhập với JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountDTO accountDTO) {
        // Sử dụng email để xác thực
        System.out.println("Email: " + accountDTO.getEmailAddress());
        System.out.println("Password: " + accountDTO.getPassword());
        AccountDTO result = accountService.login(accountDTO.getEmailAddress(), accountDTO.getPassword());

        if (result != null) {
            // Tạo JWT
            String jwt = jwtUtil.generateToken(result.getEmailAddress()); // Sử dụng email để tạo token
            System.out.println("DTO: " + result);
            return ResponseEntity.ok(new JwtResponse(jwt)); // Trả về token
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email hoặc mật khẩu không hợp lệ");
    }

    // Registration API with SMS confirmation
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody AccountDTO accountDTO) {
//        if (userRepository.findByUsername(accountDTO.getUsername()) != null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
//        }
//
//        UserEntity newUser = accountConverter.toEntity(accountDTO);
//        UserEntity savedUser = userRepository.save(newUser);
//
//        // Send confirmation SMS
//        String message = "Thank you for registering! Your phone number is: " + accountDTO.getPhoneNumber();
//        smsService.sendSms(accountDTO.getPhoneNumber(), message);
//
//        return ResponseEntity.ok(accountConverter.toDTO(savedUser));
//    }

    // View Profile
    @GetMapping("/profile/{username}")
    public ResponseEntity<?> viewProfile(@PathVariable String username) {
        UserEntity user = accountService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(accountConverter.toDTO(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // Modify Profile
    @PutMapping("/profile/{username}")
    public ResponseEntity<?> editProfile(@PathVariable String username, @RequestBody AccountDTO accountDTO) {
        UserEntity existingUser = accountService.findByUsername(username);
        if (existingUser != null) {
            UserEntity updatedUser = accountConverter.toEntity(accountDTO, existingUser);
            userRepository.save(updatedUser);
            return ResponseEntity.ok(accountConverter.toDTO(updatedUser));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

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
                UserEntity user = userRepository.findByEmailAddress(email);
                if (user == null) {
                    // Create a new user if not exists
                    user = new UserEntity();
                    user.setUsername(email+"lorem"); //đảm bảo nó khác với username, chỉ để not-null mà thôi
                    user.setPassword(email+"notnull");
                    user.setEmailAddress(email);
                    user.setFullname(name);
                    user.setCreatedDate(LocalDateTime.now());
                    user = userRepository.save(user);
                }

                // Generate JWT token
                String jwt = jwtUtil.generateToken(user.getEmailAddress());

                // Return user info and JWT
                return ResponseEntity.ok(new JwtResponse(jwt));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @PostMapping(value = "/register")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO model) {
        try {
            AccountDTO savedAccount = accountService.save(model);
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED); // Trả về HTTP status 201
        } catch (IllegalArgumentException e) {
            // Nếu có lỗi, chẳng hạn như username đã tồn tại, trả về thông báo lỗi rõ ràng
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}