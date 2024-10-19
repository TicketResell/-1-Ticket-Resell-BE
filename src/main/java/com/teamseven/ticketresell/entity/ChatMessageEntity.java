package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
public class ChatMessageEntity extends BaseEntity {

    @Column(name = "user1_id", nullable = false)
    private Long user1_id;

    @Column(name = "user2_id", nullable = false)
    private Long user2_id;

    @Column(name = "message_content", nullable = false, length = 1000)
    private String messageContent;

    @Column(name = "message_type",nullable = false)
    private String messageType;

    public enum ChatType{
        text, image, bid
    }

    // Getters và Setters


    public Long getUser1_id() {
        return user1_id;
    }

    public void setUser1_id(Long user1_id) {
        this.user1_id = user1_id;
    }

    public Long getUser2_id() {
        return user2_id;
    }

    public void setUser2_id(Long user2_id) {
        this.user2_id = user2_id;
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
