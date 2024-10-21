package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name ="balances")
public class BalanceEntity extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "current_balance", nullable = false)
    private double currentBalance;

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;

    // Getters and Setters
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    public double getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(double currentBalance) { this.currentBalance = currentBalance; }

    public LocalDateTime getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) { this.lastUpdateTime = lastUpdateTime;}
}

