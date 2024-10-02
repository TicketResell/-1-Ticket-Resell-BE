package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {
    TicketEntity findById(long id);

}