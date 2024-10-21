package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.service.impl.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    // Gửi tin nhắn qua WebSocket
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        return chatService.sendMessage(message);
    }

    // Lấy lịch sử chat qua WebSocket
    @MessageMapping("/chat/history")
    @SendTo("/topic/history")
    public List<ChatMessageDTO> getChatHistory(Long userId) {
        return chatService.getChatHistory(userId);
    }

//    // Tìm các tin nhắn giữa hai người dùng qua WebSocket
//    @MessageMapping("/chat/history/user")
//    @SendTo("/topic/history/user")
//    public List<ChatMessageDTO> findChats(Long userId, Long senderId) {
//        return chatService.getChatHistory(userId, senderId);
//    }

//    @MessageMapping("/chat/conversations")
//    @SendTo("/topic/conversations")
//    public List<ConversationDTO> getConversations(@RequestParam Long userId) {
//        List<ConversationDTO> conversations = chatService.getConversationsByUserId(userId);
//        return ResponseEntity.ok(conversations);
//    }
}
