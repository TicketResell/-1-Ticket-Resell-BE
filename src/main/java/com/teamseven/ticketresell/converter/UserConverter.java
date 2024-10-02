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
        userEntity.setStatus(UserStatus.valueOf(userDTO.getStatus()));
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
        userDTO.setStatus(userEntity.getStatus().name());
        userDTO.setVerifiedEmail(userEntity.isVerifiedEmail());
        userDTO.setRole(userEntity.getRole());

        return userDTO;
    }

    // Convert DTO to Entity (update existing entity)
    public UserEntity toEntity(UserDTO userDTO, UserEntity existingUser) {
        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getPassword() != null) {
            existingUser.setPassword(userDTO.getPassword());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhone() != null) {
            existingUser.setPhone(userDTO.getPhone());
        }
        if (userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }
        if (userDTO.getStatus() != null) {
            existingUser.setStatus(UserEntity.UserStatus.valueOf(userDTO.getStatus()));
        }
        existingUser.setVerifiedEmail(userDTO.isVerifiedEmail());

//        // Cập nhật role nếu có
//        if (userDTO.getRoleId() != null) {
//            RoleEntity role = new RoleEntity();
//            role.setRoleName(userDTO.getRoleId()); // Giả sử bạn chỉ cập nhật role bằng ID
//            existingUser.setRole(role);
//        }

        return existingUser;
    }

}
