package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.UserDTO;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.entity.UserEntity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {


    // Convert DTO to Entity
    public UserEntity toEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setRole(userDTO.getRole());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setAddress(userDTO.getAddress());
        userEntity.setStatus(userDTO.getStatus());
        userEntity.setVerifiedEmail(userDTO.isVerifiedEmail());

        return userEntity;
    }

    // Convert Entity to DTO
    public UserDTO toDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setAddress(userEntity.getAddress());
        userDTO.setStatus(userEntity.getStatus());
        userDTO.setVerifiedEmail(userEntity.isVerifiedEmail());
        userDTO.setRole(userEntity.getRole());

        return userDTO;
    }

    // Convert DTO to Entity (update existing entity)
    public UserEntity toEntity(UserDTO userDTO, UserEntity existingUser, boolean isAdmin) {
        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getPassword() != null) {
            existingUser.setPassword(userDTO.getPassword());
        }
        if (userDTO.getPhone() != null) {
            existingUser.setPhone(userDTO.getPhone());
        }
        if (userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }

        // Chỉ cho phép admin cập nhật role, status, và email
        if (isAdmin) {
            if (userDTO.getEmail() != null) {
                existingUser.setEmail(userDTO.getEmail());
            }

            if (userDTO.getStatus() != null) {
                try {
                    existingUser.setStatus(userDTO.getStatus());
                } catch (IllegalArgumentException e) {
                    // Xử lý khi giá trị status không hợp lệ
                    throw new RuntimeException("Invalid status value: " + userDTO.getStatus());
                }
            }

            // Cập nhật role nếu người dùng có vai trò admin
            if (userDTO.getRole() != null) {
                try {
                    existingUser.setRole(userDTO.getRole()); // Cập nhật vai trò trực tiếp từ DTO
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid role value: " + userDTO.getRole());
                }
            }
        }

        existingUser.setVerifiedEmail(userDTO.isVerifiedEmail());

        return existingUser;
    }



}
