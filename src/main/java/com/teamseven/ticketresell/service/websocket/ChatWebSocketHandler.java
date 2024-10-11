package com.teamseven.ticketresell.service.websocket;
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
        System.out.println("Message received: " + message.getPayload());

        // Phát tin nhắn đến tất cả các session khác
        for (WebSocketSession s : sessions.values()) {
            s.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Xử lý khi kết nối bị đóng
        sessions.remove(session.getId());
        System.out.println("Session closed: " + session.getId());
    }
}
