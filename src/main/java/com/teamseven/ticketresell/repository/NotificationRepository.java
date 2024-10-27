package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.NotificationEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUser_IdOrUserIsNull(Long userId); // Lấy thông báo cho user cụ thể và thông báo chung
    List<NotificationEntity> findByUserIsNull(); // Chỉ lấy thông báo chung
}

