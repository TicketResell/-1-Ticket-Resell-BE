package com.teamseven.ticketresell.api;

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

@RestController
@RequestMapping("/api/accounts")
public class AccountAPI {

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
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountDTO accountDTO) {
        if (userRepository.findByUsername(accountDTO.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        UserEntity newUser = accountConverter.toEntity(accountDTO);
        UserEntity savedUser = userRepository.save(newUser);

        // Send confirmation SMS
        String message = "Thank you for registering! Your phone number is: " + accountDTO.getPhoneNumber();
        smsService.sendSms(accountDTO.getPhoneNumber(), message);

        return ResponseEntity.ok(accountConverter.toDTO(savedUser));
    }

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
}