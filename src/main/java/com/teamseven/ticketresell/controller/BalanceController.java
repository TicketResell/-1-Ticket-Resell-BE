package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.entity.BalanceEntity;
import com.teamseven.ticketresell.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    @Autowired
    private BalanceRepository balanceRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCurrentBalanceByUserId(@PathVariable Long userId) {
        BalanceEntity balance = balanceRepository.findByUser_Id(userId);

        if (balance != null) {
            return ResponseEntity.ok(balance.getCurrentBalance());
        }
        return ResponseEntity.status(404).body("0");
    }
}

