package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.CategoryEntity;
import com.teamseven.ticketresell.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findById(long id);

}