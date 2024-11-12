package com.teamseven.ticketresell.dto;

import java.time.LocalDate;

public class OrderDTO {
    private Long id;
    private Long buyerId;
    private Long sellerId;
    private Long ticketId;

    //for showing items:
    private String buyerName;
    private String sellerName;
    private String ticketName;

    private int quantity;
    private double totalAmount;
    private double serviceFee;
    private String paymentStatus;
    private String orderStatus;
    private String orderMethod;
    private String shippingImg;
    private LocalDate createdDate;

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod(String orderMethod) {
        this.orderMethod = orderMethod;
    }



    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getTicketName() {
        return ticketName;
    }

    public String getShippingImg() {
        return shippingImg;
    }

    public void setShippingImg(String shippingImg) {
        this.shippingImg = shippingImg;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}

