package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.NotificationEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUser(UserEntity user);
    List<NotificationEntity> findByIsGlobalTrue();  // Lấy các thông báo toàn cầu
}

