package com.teamseven.ticketresell.entity;

import jakarta.persistence.*;

// ChatDetail entity
@Entity
@Table(name = "chat_detail")
public class ChatDetails extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "chatID", nullable = false)
    private ChatEntity chat;

    @ManyToOne
    @JoinColumn(name = "senderID", nullable = false)
    private UserEntity sender;

    @Column(name = "message", nullable = false)
    private String message;

    public ChatEntity getChat() {
        return chat;
    }

    public void setChat(ChatEntity chat) {
        this.chat = chat;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}