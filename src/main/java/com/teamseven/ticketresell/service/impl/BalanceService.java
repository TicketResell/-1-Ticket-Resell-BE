package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.entity.BalanceEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.BalanceRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    public void updateBalanceForUser(UserEntity seller, double amount) {
        // Fetch or create the balance for the seller
        BalanceEntity balance = balanceRepository.findByUser(seller);

        if (balance == null) {
            balance = new BalanceEntity();
            balance.setCreatedDate(LocalDateTime.now());
            balance.setUser(seller);
            balance.setCurrentBalance(0.0);  // Initialize with 0 if new
        }

        // Update the balance and last update time
        balance.setCurrentBalance(balance.getCurrentBalance() + amount);
        balance.setLastUpdateTime(LocalDateTime.now());

        // Save the updated balance
        balanceRepository.save(balance);
    }


}


