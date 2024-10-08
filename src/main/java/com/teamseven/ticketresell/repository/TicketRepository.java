package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    TicketEntity findById(long id);
    List<TicketEntity> findBySeller_Id(long userId);
}