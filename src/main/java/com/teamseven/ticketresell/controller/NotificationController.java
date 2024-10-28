package com.teamseven.ticketresell.controller;
import com.teamseven.ticketresell.converter.NotificationConverter;
import com.teamseven.ticketresell.dto.NotificationDTO;
import com.teamseven.ticketresell.entity.NotificationEntity;
import com.teamseven.ticketresell.repository.NotificationRepository;
import com.teamseven.ticketresell.service.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationConverter notificationConverter;
    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody NotificationDTO notificationDTO) {
        try {
            notificationDTO.setCreatedDate(LocalDateTime.now());
            NotificationEntity notificationEntity = notificationConverter.toEntity(notificationDTO);
            NotificationEntity savedNotification = notificationRepository.save(notificationEntity);
            return ResponseEntity.ok(savedNotification);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable Long userId) {
        try {
            List<NotificationEntity> notifications = notificationService.getNotificationsForUser(userId);
                return ResponseEntity.ok(notifications);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<NotificationEntity>> getAllNotifications() {
        List<NotificationEntity> notifications = notificationRepository.findAll();
        return ResponseEntity.ok(notifications);
    }
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotifications(@PathVariable Long notificationId) {
        notificationRepository.deleteById(notificationId);
        return ResponseEntity.ok("This notification was deleted successfully!");
    }
}
