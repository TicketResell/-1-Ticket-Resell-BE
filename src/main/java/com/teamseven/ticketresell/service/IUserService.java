package com.teamseven.ticketresell.service;


import com.teamseven.ticketresell.dto.JwtResponse;
import com.teamseven.ticketresell.dto.UserDTO;
import com.teamseven.ticketresell.entity.UserEntity;

import java.time.LocalDateTime;

public interface IUserService {
    UserDTO login(String identifier, String password);
    UserDTO register(UserDTO userDTO);
    UserEntity createNewAccount(String username, String password);
    UserEntity findById(Long id);
    UserEntity save(UserEntity user);
    void delete(Long id);
    String getUserRoleByUsername(String username);
    UserEntity updateUser(Long id, UserDTO userDTO);
//    void resetPassword(String email);
    void updatePassword(String email, String newPassword);
    void verifyEmail(String email);
    UserDTO viewProfile(String username, String currentUser);
    UserDTO editProfile(String username, UserDTO userDTO);
    JwtResponse loginWithGoogle(String idTokenString);
    String getUserNameByID(Long id);
    String getFullNameByID(Long id);
    Boolean isFullData(Long id);
    String getAvatarByID(Long id);
    Boolean isOnline(Long id);
    LocalDateTime lastSeen(Long id);
    void banUser(Long id);
    Integer countUser();
}
