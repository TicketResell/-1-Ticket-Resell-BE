package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Transaction entity
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private OrderEntity order;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    public enum TransactionType {
        DEPOSIT, REFUND, PAYMENT, COMMISSION
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}