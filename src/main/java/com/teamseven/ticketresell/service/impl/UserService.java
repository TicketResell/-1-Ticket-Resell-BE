package com.teamseven.ticketresell.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.teamseven.ticketresell.converter.UserConverter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.teamseven.ticketresell.dto.JwtResponse;
import com.teamseven.ticketresell.dto.UserDTO;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.IUserService;
import com.teamseven.ticketresell.util.JwtUtil;
import com.teamseven.ticketresell.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter accountConverter;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtil jwtUtil;

    private String googleClientId = "310764216947-glor2ci0tha7scaf77cgmiqrod6c58fq.apps.googleusercontent.com";

    @Override
    public UserDTO login(String identifier, String password) {
        UserEntity user;

        // Biểu thức chính quy để kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (pattern.matcher(identifier).matches()) {
            user = userRepository.findByEmail(identifier);
        } else {
            user = userRepository.findByUsername(identifier);
        }

        if (user != null && password.equals(user.getPassword())) {
            if ("banned".equals(user.getStatus())) {
                throw new RuntimeException("Your account has been banned.");
            } else if (!user.isVerifiedEmail()) {
                throw new RuntimeException("You are not verified.");
            }
            return accountConverter.toDTO(user);
        }
        throw new RuntimeException("Email or username is invalid.");
    }



    @Override
    public UserDTO register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        if(userRepository.findByPhone(userDTO.getPhone()) != null){
            throw new RuntimeException("Phone already exists");
        }

        UserEntity newUser = accountConverter.toEntity(userDTO);
        newUser.setCreatedDate(LocalDateTime.now());
        newUser.setRole("user");
        newUser.setStatus("active");
        userDTO.setVerifiedEmail(false);
        UserEntity savedUser = userRepository.save(newUser);

        String verificationLink = "http://localhost:8084/api/accounts/verify?email=" + userDTO.getEmail();
        String subject = "Email Verification";
        String body = "Thank you for registering. Please click the link below to verify your email:\n" + verificationLink;
        emailService.sendEmail(userDTO.getEmail(), subject, body);

        return accountConverter.toDTO(savedUser);
    }

    @Override
    public UserEntity createNewAccount(String username, String password) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(password);
        return userRepository.save(newUser);
    }
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Cập nhật hoặc lưu người dùng
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        UserEntity existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public String getUserRoleByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        return user != null ? user.getRole() : null;
    }

    @Override
    public UserEntity updateUser(Long id, UserDTO userDTO) {
        // check is exist?
        UserEntity existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }
        // update
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setFullname(userDTO.getFullname());
        existingUser.setUserImage(userDTO.getUserImage());
        existingUser.setStatus(userDTO.getStatus());
        existingUser.setVerifiedEmail(userDTO.isVerifiedEmail());
        existingUser.setRole(userDTO.getRole());
        // save to db
        return userRepository.save(existingUser);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Invalid email!");
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public void verifyEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email not found");
        }
        user.setVerifiedEmail(true);
        userRepository.save(user);
    }

    @Override
    public UserDTO viewProfile(String username, String currentUser) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String role = getUserRoleByUsername(user.getUsername());

        if (role.equals("ADMIN") || role.equals("STAFF") || currentUser.equals(user.getUsername())) {
            return accountConverter.toDTO(user);
        }

        throw new RuntimeException("You are not allowed to view this profile");
    }

    @Override
    public UserDTO editProfile(String username, UserDTO userDTO) {
        UserEntity existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // Cập nhật thông tin cho existingUser
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setFullname(userDTO.getFullname());
        existingUser.setUserImage(userDTO.getUserImage());
        // Lưu cập nhật vào cơ sở dữ liệu
        userRepository.save(existingUser);

        // Chuyển đổi và trả về DTO của người dùng đã cập nhật
        return accountConverter.toDTO(existingUser);
    }
    @Override
    public UserDTO editBuyProfile(String username, UserDTO userDTO) {
        UserEntity existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // Cập nhật thông tin cho existingUser
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setFullname(userDTO.getFullname());
        userRepository.save(existingUser);

        // Chuyển đổi và trả về DTO của người dùng đã cập nhật
        return accountConverter.toDTO(existingUser);
    }


    @Override
    public JwtResponse loginWithGoogle(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                // Check if user exists
                UserEntity user = userRepository.findByEmail(email);
                if (user == null) {
                    // Create a new user if not exists
                    user = new UserEntity();
                    user.setUsername(email + "_google");
                    user.setPassword(email + "_password");
                    user.setEmail(email);
                    user.setStatus("active");
                    user.setRole("user"); // default == user
                    user.setCreatedDate(LocalDateTime.now());
                    user.setVerifiedEmail(true);
                    user.setUserImage(pictureUrl);
                    user = userRepository.save(user);
                } else {
                    // Nếu user đã tồn tại, có thể cập nhật lại ảnh từ Google
                    user.setUserImage(pictureUrl);
                    userRepository.save(user);
                }

                // Generate JWT token
                String jwt = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getFullname(), user.getUserImage(), user.getRole());

                // Return user info and JWT
                return new JwtResponse(jwt);
            } else {
                throw new RuntimeException("Invalid Google token");
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUserNameByID(Long id) {
       UserEntity user = userRepository.findById(id).orElse(null);
       return user.getUsername();
    }

    @Override
    public String getFullNameByID(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        assert user != null;
        return user.getFullname();
    }

    @Override
    public Boolean isFullData(Long id) {
        UserEntity user = findById(id);

        if(user.getPhone() == null || user.getAddress() == null
                || user.getFullname() == null) return false;

        if(user.getPhone().isEmpty() || user.getAddress().isEmpty()
                || user.getFullname().isEmpty()) return false;
        return true;
    }
    @Override
    public String getAvatarByID(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        return user.getUserImage();
    }

    @Override
    public Boolean isOnline(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        return user.isOnline();
    }

    @Override
    public LocalDateTime lastSeen(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        return user.getLastSeen();
    }

    @Override
    public void banUser(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if(user == null) throw new ResourceNotFoundException("User not found");

        if (!user.getRole().equalsIgnoreCase("user")) {
            throw new IllegalArgumentException("Cannot ban an admin/staff user.");
        }

        user.setStatus("banned");
        userRepository.save(user);
        userRepository.flush(); //tống vào db ngay lập tức
    }


    @Override
    public int countUser() {
        List<UserEntity> userEntities = userRepository.findAll();
        int count = 0;
        for(UserEntity user : userEntities){
            count++;
        }
        return count;
    }

    @Override
    public UserEntity setUserAgency(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setAgency(true);
        return userRepository.save(user);
    }


}
