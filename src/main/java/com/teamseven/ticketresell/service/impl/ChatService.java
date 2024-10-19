package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.converter.ChatMessageConverter;
import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.repository.ChatMessageRepository;
import com.teamseven.ticketresell.service.IChatService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService implements IChatService {

    private final ChatMessageConverter chatMessageConverter;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    // Constructor injection
    public ChatService(ChatMessageConverter chatMessageConverter, ChatMessageRepository chatMessageRepository, UserService userService) {
        this.chatMessageConverter = chatMessageConverter;
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
    }

    @Override
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }

        // Chuyển đổi từ DTO sang Entity
        ChatMessageEntity chatMessageEntity = chatMessageConverter.toEntity(message);

        // Lưu message vào cơ sở dữ liệu
        ChatMessageEntity savedMessage = chatMessageRepository.save(chatMessageEntity);

        // Chuẩn bị DTO trả về cho client
        return chatMessageConverter.toDTO(savedMessage);
    }

    @Override
    @Cacheable(value = "chatHistory", key = "#userId")
    public List<ChatMessageDTO> getChatHistory(Long userId) {
        System.out.println("Fetching chat history for userId: " + userId); // Log userId

        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findBySenderIdOrReceiverId(userId, userId);
        System.out.println("Found " + chatMessageEntities.size() + " messages for userId: " + userId); // Log số lượng tin nhắn tìm thấy


        // Chuyển đổi danh sách ChatMessageEntity thành danh sách ChatMessageDTO
        List<ChatMessageDTO> chatMessageDTOs = chatMessageEntities.stream()
                .map(chatMessageConverter::toDTO)
                .collect(Collectors.toList());

        System.out.println("Returning " + chatMessageDTOs.size() + " messages as DTOs for userId: " + userId); // Log số lượng tin nhắn DTO trả về
        return chatMessageDTOs;
    }


    // Lấy lịch sử chat giữa 2 người dùng (sender và receiver)
    @Override
    public List<ChatMessageDTO> getChatHistory(Long senderID, Long receiverID) {
        List<ChatMessageEntity> chats = chatMessageRepository.findBySenderIdAndReceiverId(senderID, receiverID);
        return chats.stream()
                .map(chatMessageConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String getUser2FullName(Long userId) {
       return userService.getFullNameByID(userId);
    }

}
