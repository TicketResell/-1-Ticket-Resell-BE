package com.teamseven.ticketresell.dto;

import java.time.LocalDateTime;

public class ConversationDTO {

    //ai bảo code phèn thì xin thưa frontend k nhận được Map<Long, String> !!!
    private Long user1;
    private Long user2;
    private String user1FullName;
    private String user2FullName;
    private String user2Img;
    private boolean user2OnlineStatus;
    private int unreadCount;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUser1FullName() {
        return user1FullName;
    }

    public void setUser1FullName(String user1FullName) {
        this.user1FullName = user1FullName;
    }

    public Long getUser2() {
        return user2;
    }

    public void setUser2(Long user2) {
        this.user2 = user2;
    }

    public String getUser2FullName() {
        return user2FullName;
    }

    public void setUser2FullName(String user2FullName) {
        this.user2FullName = user2FullName;
    }

    public Long getUser1() {
        return user1;
    }

    public void setUser1(Long user1) {
        this.user1 = user1;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getUser2Img(String avatarByID) {
        return user2Img;
    }

    public void setUser2Img(String user2Img) {
        this.user2Img = user2Img;
    }

    public boolean isUser2OnlineStatus(Boolean online) {
        return user2OnlineStatus;
    }

    public void setUser2OnlineStatus(boolean user2OnlineStatus) {
        this.user2OnlineStatus = user2OnlineStatus;
    }
}
