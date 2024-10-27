package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.NotificationDTO;
import com.teamseven.ticketresell.entity.NotificationEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.CategoryRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {
    @Autowired
    private UserRepository userRepository;
    // Chuyển từ DTO sang Entity
    public NotificationEntity toEntity(NotificationDTO dto) {
        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(dto.getTitle());
        entity.setMessage(dto.getMessage());
        if (dto.getUserId() != null) {
            entity.setUser(userRepository.findById(dto.getUserId()).orElse(null));
        }
        return entity;
    }
}

