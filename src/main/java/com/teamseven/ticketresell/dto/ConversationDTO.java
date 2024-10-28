package com.teamseven.ticketresell.dto;

import java.util.List;
import java.util.Map;

public class ConversationDTO {
    private Map<Long, String> user1;
    private Map<Long, String> user2;
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

    public Map<Long, String> getUser2() {
        return user2;
    }

    public void setUser2(Map<Long, String> user2) {
        this.user2 = user2;
    }

    public Map<Long, String> getUser1() {
        return user1;
    }

    public void setUser1(Map<Long, String> user1) {
        this.user1 = user1;
    }
}
