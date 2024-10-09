package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    TicketEntity findById(long id);
    List<TicketEntity> findByCategoryId(Long categoryId);
    List<TicketEntity> findByUserIdAndStatus(Long userId); // Lấy danh sách vé theo status
    List<TicketEntity> findBySeller_Id(long userId);

    List<TicketEntity> findByStatus(String status);

}