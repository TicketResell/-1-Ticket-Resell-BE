package com.teamseven.ticketresell.dto;

import java.time.LocalDateTime;

public class UserOnlineDTO {
    private boolean isOnline;
    private LocalDateTime lastSeen;

    public UserOnlineDTO(boolean isOnline, LocalDateTime lastSeen) {
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
    }
    public boolean isOnline() {
        return isOnline;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }
}
