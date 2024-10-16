package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByOrderId(Long orderId);
    List<TransactionEntity> findByBuyerId(Long buyerId);
    List<TransactionEntity> findBySellerId(Long sellerId);
}

