package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserEntity buyer; // Người mua

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller; // Người bán

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketEntity ticket; // Vé đã mua



    @Column(name = "quantity", nullable = false)
    private int quantity; // Số lượng vé mua

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private double totalAmount; // Tổng tiền của đơn hàng

    @Column(name = "service_fee", precision = 10, scale = 2)
    private double serviceFee; // Phí dịch vụ

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // Trạng thái thanh toán

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // Trạng thái đơn hàng

    @Column(name = "order_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderMethod orderMethod; // Phương thức thanh toán

    public enum OrderStatus {
        pending,shipping,received, completed, cancelled
    }

    public enum PaymentStatus {
        pending, paid, failed
    }

    public enum OrderMethod {
        COD, vnpay
    }

    // Getters và Setters
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

    public TicketEntity getTicket() {
        return ticket;
    }

    public void setTicket(TicketEntity ticket) {
        this.ticket = ticket;
    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderMethod getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod(OrderMethod orderMethod) {
        this.orderMethod = orderMethod;
    }
}
