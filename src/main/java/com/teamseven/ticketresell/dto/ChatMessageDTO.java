    package com.teamseven.ticketresell.dto;

    import java.time.LocalDateTime;

    public class ChatMessageDTO {
        private Long chatId;
        private Long user1_id;
        private String user1_avatar;
        private String user2_avatar;
        private Long user2_id;
        private String messageContent;
        private String messageType;
        private String user2Name;
        private LocalDateTime timestamp;

        //new
        private boolean isread;

        public Long getChatId() {
            return chatId;
        }

        public void setChatId(Long chatId) {
            this.chatId = chatId;
        }

        public Long getUser1_id() {
            return user1_id;
        }

        public void setUser1_id(Long user1_id) {
            this.user1_id = user1_id;
        }

        public String getUser1_avatar() {
            return user1_avatar;
        }

        public void setUser1_avatar(String user1_avatar) {
            this.user1_avatar = user1_avatar;
        }

        public Long getUser2_id() {
            return user2_id;
        }

        public void setUser2_id(Long user2_id) {
            this.user2_id = user2_id;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getUser2Name() {
            return user2Name;
        }

        public void setUser2Name(String user2Name) {
            this.user2Name = user2Name;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public boolean isIsread() {
            return isread;
        }

        public void setIsread(boolean isread) {
            this.isread = isread;
        }

        public String getUser2_avatar() {
            return user2_avatar;
        }

        public void setUser2_avatar(String user2_avatar) {
            this.user2_avatar = user2_avatar;
        }
    }
