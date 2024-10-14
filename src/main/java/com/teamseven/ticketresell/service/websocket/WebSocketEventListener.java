//package com.teamseven.ticketresell.service.websocket;
//
//
//import com.teamseven.ticketresell.dto.ChatMessageDTO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//import java.time.LocalDateTime;
//
//@Component
//public class WebSocketEventListener {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
//
//    @Autowired
//    private SimpMessageSendingOperations messagingTemplate;
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        logger.info("Received a new web socket connection");
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        Long senderId = (Long) headerAccessor.getSessionAttributes().get("senderId");
//        Long receiverId = (Long) headerAccessor.getSessionAttributes().get("receiverId");
//
//        if (senderId != null && receiverId != null) {
//            logger.info("User Disconnected: Sender ID: " + senderId + ", Receiver ID: " + receiverId);
//
//            // Tạo ChatMessageDTO khi ngắt kết nối
//            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
//            chatMessageDTO.setSenderId(senderId);
//            chatMessageDTO.setReceiverId(receiverId);
//            chatMessageDTO.setMessageContent("User has disconnected.");
//            chatMessageDTO.setTimestamp(LocalDateTime.now());
//
//            // Gửi thông báo cho cả hai người dùng
//            messagingTemplate.convertAndSend("/topic/chat/" + receiverId, chatMessageDTO);
//            messagingTemplate.convertAndSend("/topic/chat/" + senderId, chatMessageDTO);
//        }
//    }
//}
