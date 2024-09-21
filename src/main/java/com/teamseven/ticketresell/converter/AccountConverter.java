package com.teamseven.ticketresell.converter;

import org.springframework.stereotype.Component;
import com.teamseven.ticketresell.dto.AccountDTO;
import com.teamseven.ticketresell.entity.UserEntity;

@Component
public class AccountConverter {

    // Convert from DTO to Entity (AccountDTO -> UserEntity)
    public UserEntity toEntity(AccountDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFullname(dto.getFullname());
        entity.setRoleId(dto.getRoleId());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmailAddress(dto.getEmailAddress());
        return entity;
    }

    // Convert from Entity to DTO (UserEntity -> AccountDTO)
    public AccountDTO toDTO(UserEntity entity) {
        AccountDTO dto = new AccountDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setFullname(entity.getFullname());
        dto.setRoleId(entity.getRoleId());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmailAddress(entity.getEmailAddress());
        return dto;
    }

    // Update existing entity from DTO (AccountDTO -> UserEntity)
    public UserEntity toEntity(AccountDTO dto, UserEntity entity) {
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFullname(dto.getFullname());
        entity.setRoleId(dto.getRoleId());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmailAddress(dto.getEmailAddress());
        return entity;
    }
}
