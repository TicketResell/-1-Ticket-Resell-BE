package com.teamseven.ticketresell.dto;

import java.util.Map;

public class ConversationDTO {
    private Map<Long, Long> users;
    private int unreadCount;
    private String lastMessage;

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

    public Map<Long, Long> getUsers() {
        return users;
    }

    public void setUsers(Map<Long, Long> users) {
        this.users = users;
    }
}
