package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;

// Rating entity
@Entity
@Table(name = "ratings")
public class RatingEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private UserEntity buyer;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private OrderEntity order;

    @Column(name = "rating_score", nullable = false)
    private int ratingScore;

    @Column(name = "feedback")
    private String feedback;

    public UserEntity getBuyer() {
        return buyer;
    }

    public void setBuyer(UserEntity buyer) {
        this.buyer = buyer;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}