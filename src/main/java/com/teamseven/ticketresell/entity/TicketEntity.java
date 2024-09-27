package com.teamseven.ticketresell.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
public class TicketEntity extends BaseEntity {

    @Column(name = "userID", nullable = false)
    private int userId;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "eventTitle", length = 255, nullable = false)
    private String eventTitle;

    @Column(name = "eventDate", nullable = false)
    private LocalDate eventDate;

    @Column(name = "categoryID", nullable = false)
    private int categoryId;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "ticketType", length = 100)
    private String ticketType;

    @Column(name = "salePrice", precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "ticketDetails")
    private String ticketDetails;

    @Column(name = "imageID")
    private int imageId;

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(String ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
