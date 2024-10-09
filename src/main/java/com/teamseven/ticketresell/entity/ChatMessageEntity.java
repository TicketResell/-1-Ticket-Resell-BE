//    package com.teamseven.ticketresell.entity;
//
//    import jakarta.persistence.*;
//    import java.time.LocalDateTime;
//
//    @Entity
//    @Table(name = "chat_message")
//    public class ChatMessageEntity extends BaseEntity {
//
//        @Column(name = "buyer_id", nullable = false)
//        private Long buyerId;
//
//        @Column(name = "seller_id", nullable = false)
//        private Long sellerId;
//
//        @Column(name = "sender_id", nullable = false)
//        private Long senderId;
//
//        @Column(name = "message", nullable = false)
//        private String message;
//
//        @Column(name = "timestamp", nullable = false)
//        private LocalDateTime timestamp;
//
//        // Getters và Setters
//        public Long getChatId(){
//            return getId();
//        }
//
//        public Long getBuyerId() {
//            return buyerId;
//        }
//
//        public void setBuyerId(Long buyerId) {
//            this.buyerId = buyerId;
//        }
//
//        public Long getSellerId() {
//            return sellerId;
//        }
//
//        public void setSellerId(Long sellerId) {
//            this.sellerId = sellerId;
//        }
//
//        public Long getSenderId() {
//            return senderId;
//        }
//
//        public void setSenderId(Long senderId) {
//            this.senderId = senderId;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//
//        public LocalDateTime getTimestamp() {
//            return timestamp;
//        }
//
//        public void setTimestamp(LocalDateTime timestamp) {
//            this.timestamp = timestamp;
//        }
//    }
