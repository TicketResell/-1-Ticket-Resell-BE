package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionEntity extends BaseEntity{


    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserEntity buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller;


    @Column(name = "transaction_amount", nullable = false)
    private double transactionAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;
    @Column(name = "vnp_response_code")  // Mã phản hồi từ VNPay
    private String vnpResponseCode;

    public String getVnpResponseCode() {
        return vnpResponseCode;
    }

    public void setVnpResponseCode(String vnpResponseCode) {
        this.vnpResponseCode = vnpResponseCode;
    }

    public String getVnpTransactionNo() {
        return vnpTransactionNo;
    }

    public void setVnpTransactionNo(String vnpTransactionNo) {
        this.vnpTransactionNo = vnpTransactionNo;
    }

    @Column(name = "vnp_transaction_no")  // Số giao dịch từ VNPay
    private String vnpTransactionNo;
    public enum TransactionType {
        Income, Expense, Refund
    }
    // Income : buyer to system
    // Expense: system to seller
    // Refund: buyer cancel
    // Getters và Setters

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public UserEntity getBuyer() {
        return buyer;
    }

    public void setBuyer(UserEntity buyer) {
        this.buyer = buyer;
    }

    public UserEntity getSeller() {
        return seller;
    }

    public void setSeller(UserEntity seller) {
        this.seller = seller;
    }


    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}

