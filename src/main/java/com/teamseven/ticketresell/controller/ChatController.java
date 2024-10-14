package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.service.impl.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        return chatService.sendMessage(message);
    }

    @GetMapping("/chat/history")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(@RequestParam Long userId) {
        List<ChatMessageDTO> chatHistory = chatService.getChatHistory(userId);
        return ResponseEntity.ok(chatHistory);
    }

    @GetMapping("/chat/history/user")
    public ResponseEntity<List<ChatMessageDTO>> findChats(@RequestParam Long userId, @RequestParam Long senderId) {
        List<ChatMessageDTO> chatHistory = chatService.getChatHistory(userId, senderId);
        return ResponseEntity.ok(chatHistory);
    }
}