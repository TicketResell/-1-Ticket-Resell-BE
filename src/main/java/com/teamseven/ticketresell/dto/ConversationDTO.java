package com.teamseven.ticketresell.dto;

import java.util.List;

public class ConversationDTO {
    private List<Long> users;
    private int unreadCount;
    private String lastMessage;
    private String user2FullName;

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

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public String getUser2FullName() {
        return user2FullName;
    }

    public void setUser2FullName(String user2FullName) {
        this.user2FullName = user2FullName;
    }
}
