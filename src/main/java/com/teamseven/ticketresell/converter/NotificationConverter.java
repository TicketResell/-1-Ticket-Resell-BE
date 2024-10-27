package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.NotificationDTO;
import com.teamseven.ticketresell.entity.NotificationEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {

    // Chuyển từ Entity sang DTO
    public NotificationDTO toDTO(NotificationEntity entity) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setGlobal(entity.isGlobal());
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        return dto;
    }

    // Chuyển từ DTO sang Entity
    public NotificationEntity toEntity(NotificationDTO dto, UserEntity user) {
        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(dto.getTitle());
        entity.setMessage(dto.getMessage());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setGlobal(dto.isGlobal());
        entity.setUser(user);
        return entity;
    }
}

