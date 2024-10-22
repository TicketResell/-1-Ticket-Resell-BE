package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.dto.ConversationDTO;
import com.teamseven.ticketresell.repository.ChatMessageRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.ChatService;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserService userService;

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



    //ONLY FOR STAFF!!!
    @PostMapping("/get-chat-history")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistoryByPairUserId(Long userId, Long user2Id) {
        return ResponseEntity.ok(chatService.getChatHistory(userId, user2Id));
    }

    //MUST READ DTO -> CONVERSATION DTO DATA TYPE
    @MessageMapping("/chat/conversations")
    @SendTo("/topic/conversations")
    public List<ConversationDTO> getConversations(@RequestParam Long userId) {
        return chatService.getConversationsByUserId(userId);
    }

    @MessageMapping("/chat/online-status")
    @SendTo("/topic/online-status")
    public String getOnlineStatus(@RequestParam Long userId) {
        boolean isOnline = userService.isOnline(userId);
        LocalDateTime lastSeen  = LocalDateTime.now();
        // Trả về trạng thái của người dùng
        return isOnline ? "User " + userId + " is online." : "User " + userId + " is offline." + "last seen: " + lastSeen;
    }
}
