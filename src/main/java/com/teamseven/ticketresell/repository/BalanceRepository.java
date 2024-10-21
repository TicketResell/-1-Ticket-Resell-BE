package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.BalanceEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {
    BalanceEntity findByUser(UserEntity userEntity);
}


