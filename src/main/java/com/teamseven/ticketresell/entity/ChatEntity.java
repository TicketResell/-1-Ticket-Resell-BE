package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;

import java.util.List;

// Chat entity
@Entity
@Table(name = "chat")
public class ChatEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "buyerID", nullable = false)
    private UserEntity buyer;

    @ManyToOne
    @JoinColumn(name = "sellerID", nullable = false)
    private UserEntity seller;

    @OneToMany(mappedBy = "chat")
    private List<ChatDetails> chatDetails;

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

    public List<ChatDetails> getChatDetails() {
        return chatDetails;
    }

    public void setChatDetails(List<ChatDetails> chatDetails) {
        this.chatDetails = chatDetails;
    }
}