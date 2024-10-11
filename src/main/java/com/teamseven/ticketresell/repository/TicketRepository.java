package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    TicketEntity findById(long id);
    List<TicketEntity> findByCategoryId(long categoryId);
    List<TicketEntity> findBySeller_IdAndStatus(Long sellerId, String status);
    List<TicketEntity> findBySeller_Id(long userId);

    List<TicketEntity> findByStatus(String status);
    List<TicketEntity> findByEventTitleContainingOrTicketDetailsContaining(String eventTitle, String ticketDetails);
    List<TicketEntity> findByEventDateAfterOrderByEventDateAsc(LocalDate today);
    Page<TicketEntity> findAll(Pageable pageable);
}