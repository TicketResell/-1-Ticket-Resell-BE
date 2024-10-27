package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.converter.NotificationConverter;
import com.teamseven.ticketresell.dto.NotificationDTO;
import com.teamseven.ticketresell.entity.NotificationEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.NotificationRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationConverter notificationConverter;

    public NotificationEntity createNotification(NotificationDTO dto) {
        NotificationEntity notification = notificationConverter.toEntity(dto);
        return notificationRepository.save(notification);
    }

    public List<NotificationEntity> getNotificationsForUser(Long userId) {
        if (userId != null) {
            // Lấy thông báo cho user cụ thể và thông báo chung
            return notificationRepository.findByUser_IdOrUserIsNull(userId);
        } else {
            // Lấy tất cả thông báo chung
            return notificationRepository.findByUserIsNull();
        }
    }
}

