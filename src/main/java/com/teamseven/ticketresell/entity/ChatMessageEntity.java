package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
public class ChatMessageEntity extends BaseEntity {

    @Column(name = "user1_id", nullable = false)
    private Long user1;

    @Column(name = "user2_id", nullable = false)
    private Long user2;

    @Column(name = "message_content", nullable = false, length = 1000)
    private String messageContent;

    @Column(name = "message_type",nullable = false)
    private String messageType;

    public enum ChatType{
        text, image, bid
    }

    // Getters và Setters


    public Long getUser1() {
        return user1;
    }

    public void setUser1(Long user1) {
        this.user1 = user1;
    }

    public Long getUser2() {
        return user2;
    }

    public void setUser2(Long user2) {
        this.user2 = user2;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getTimestamp(){ return  super.getCreatedDate();}

}
