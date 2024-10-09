package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    TicketEntity findById(long id);
    List<TicketEntity> findByCategoryId(long categoryId);
    List<TicketEntity> findBySeller_IdAndStatus(Long sellerId, String status);
    List<TicketEntity> findBySeller_Id(long userId);

    List<TicketEntity> findByStatus(String status);

}