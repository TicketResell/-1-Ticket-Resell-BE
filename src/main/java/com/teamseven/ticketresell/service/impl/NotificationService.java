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

    // Tạo thông báo
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        UserEntity user = null;
        if (notificationDTO.getUserId() != null) {
            user = userRepository.findById(notificationDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }
        NotificationEntity notification = notificationConverter.toEntity(notificationDTO, user);
        NotificationEntity savedNotification = notificationRepository.save(notification);
        return notificationConverter.toDTO(savedNotification);
    }

    // Xóa thông báo theo ID
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new IllegalArgumentException("Notification not found");
        }
        notificationRepository.deleteById(notificationId);
    }

    // Lấy thông báo cho tất cả người dùng hoặc cho một người dùng cụ thể
    public List<NotificationDTO> getNotifications(Long userId) {
        List<NotificationEntity> notifications;
        if (userId == null) {
            notifications = notificationRepository.findAll();
        } else {
            notifications = notificationRepository.findByIsGlobalTrue();
        }
        return notifications.stream()
                .map(notificationConverter::toDTO)
                .collect(Collectors.toList());
    }
}

