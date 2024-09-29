package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
public class TicketEntity extends BaseEntity {

    @Column(name = "userID", nullable = false)
    private long userId;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "eventTitle", length = 255, nullable = false)
    private String eventTitle;

    @Column(name = "eventDate", nullable = false)
    private LocalDate eventDate;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
//    @Column(name = "categoryID", nullable = false)
//    private long categoryId;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "ticketType", length = 100)
    private String ticketType;

    @Column(name = "salePrice", precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "ticketDetails")
    private String ticketDetails;

    @Column(name = "imageID")
    private long imageId;

    // Getters and Setters

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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
    public String getDescription() {
        return description;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

//    public long getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(long categoryId) {
//        this.categoryId = categoryId;
//    }

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

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}
