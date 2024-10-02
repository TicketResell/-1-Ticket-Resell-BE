package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Order entity
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "buyerID", nullable = false)
    private UserEntity buyer;

    @ManyToOne
    @JoinColumn(name = "sellerID", nullable = false)
    private UserEntity seller;

    @ManyToOne
    @JoinColumn(name = "ticketID", nullable = false)
    private TicketEntity ticket;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderMethod orderMethod;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private double totalAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public enum OrderMethod {
        COD, PAYPAL, VNPAY
    }

    public enum OrderStatus {
        PENDING, COMPLETED, CANCELLED
    }

    public UserEntity getSeller() {
        return seller;
    }

    public void setSeller(UserEntity seller) {
        this.seller = seller;
    }

    public TicketEntity getTicket() {
        return ticket;
    }

    public void setTicket(TicketEntity ticket) {
        this.ticket = ticket;
    }

    public UserEntity getBuyer() {
        return buyer;
    }

    public void setBuyer(UserEntity buyer) {
        this.buyer = buyer;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderMethod getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod(OrderMethod orderMethod) {
        this.orderMethod = orderMethod;
    }
}
