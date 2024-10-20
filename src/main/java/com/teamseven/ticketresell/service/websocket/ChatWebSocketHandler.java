package com.teamseven.ticketresell.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamseven.ticketresell.dto.ChatMessageDTO;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Kết nối WebSocket mới được thiết lập
        sessions.put(session.getId(), session);
        System.out.println("New session established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Xử lý tin nhắn văn bản gửi đến
        String payload = message.getPayload();
        System.out.println("Received payload: " + payload);

        ObjectMapper objectMapper = new ObjectMapper();
        ChatMessageDTO chatMessage;

        try {
            // Parse JSON từ payload thành đối tượng ChatMessageDTO
            chatMessage = objectMapper.readValue(payload, ChatMessageDTO.class);

            // In ra các giá trị từ JSON để kiểm tra
            System.out.println("Sender ID: " + chatMessage.getUser1_id());
            System.out.println("Receiver ID: " + chatMessage.getUser2_id());
            System.out.println("Message Content: " + chatMessage.getMessageContent());

            // Tạo tin nhắn JSON mới để phát lại
            String messageToSend = objectMapper.writeValueAsString(chatMessage);
            TextMessage textMessage = new TextMessage(messageToSend);

            // Phát tin nhắn đến tất cả các session khác
            for (WebSocketSession s : sessions.values()) {
                if (!s.getId().equals(session.getId())) { // Tránh gửi lại tin nhắn cho chính session đã gửi
                    s.sendMessage(textMessage);
                    System.out.println("Message sent to session " + s.getId());
                }
            }
        } catch (Exception e) {
            // Bắt lỗi nếu có vấn đề khi parse JSON
            System.err.println("Error parsing message: " + e.getMessage());
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Xử lý khi kết nối bị đóng
        sessions.remove(session.getId());
        System.out.println("Session closed: " + session.getId() + " with status " + status);
    }
}