package com.teamseven.ticketresell.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

// Payment entity
@Entity
@Table(name = "payments")
public class PaymentEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private OrderEntity order;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private double amount;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public enum PaymentStatus {
        PAID, CANCELLED, REFUND
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

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}