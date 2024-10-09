package com.teamseven.ticketresell.api;

import com.teamseven.ticketresell.converter.ChatMessageConverter;
import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.repository.ChatMessageRepository;
import com.teamseven.ticketresell.service.impl.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageConverter chatMessageConverter;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // API WebSocket để gửi tin nhắn
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        // Gán timestamp hiện tại
        message.setTimestamp(LocalDateTime.now());

        // Chuyển đổi từ DTO sang Entity
        ChatMessageEntity chatMessageEntity = chatMessageConverter.toEntity(message);

        // Lưu message vào cơ sở dữ liệu thông qua ChatService
        ChatMessageEntity savedMessage = chatMessageRepository.save(chatMessageEntity);

        // Chuẩn bị DTO trả về cho client
        ChatMessageDTO response = chatMessageConverter.toDTO(savedMessage);

        return response;
    }

    // API lấy lịch sử chat theo chatId
    @GetMapping("/chat/history")
    public List<ChatMessageDTO> getChatHistory(@RequestParam Long chatId) {
        List<ChatMessageEntity> chatHistory = chatMessageRepository.findByChatId(chatId);

        // Chuyển đổi từ ChatMessageEntity sang ChatMessageDTO để trả về cho client
        return chatHistory.stream()
                .map(chatMessageConverter::toDTO) // Sử dụng converter để chuyển đổi
                .collect(Collectors.toList());
    }
}
