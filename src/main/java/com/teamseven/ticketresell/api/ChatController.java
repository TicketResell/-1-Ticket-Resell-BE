package com.teamseven.ticketresell.api;

import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.service.impl.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    // API WebSocket để gửi tin nhắn
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        return chatService.sendMessage(message);
    }

    // API lấy lịch sử chat theo chatId
    @GetMapping("/chat/history")
    public ChatMessageDTO getChatHistory(@RequestParam Long chatId) {
        return chatService.getChatHistory(chatId);
    }

    // API tìm kiếm chat theo userId và senderId
    @GetMapping("/chat/search")
    public List<ChatMessageDTO> findChats(@RequestParam Long userId, @RequestParam Long senderId) {
        return chatService.getChatHistory(userId, senderId);
    }
}
