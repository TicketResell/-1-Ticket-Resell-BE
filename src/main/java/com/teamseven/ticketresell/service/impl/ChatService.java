package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.converter.ChatMessageConverter;
import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.repository.ChatMessageRepository;
import com.teamseven.ticketresell.service.IChatService;
import com.teamseven.ticketresell.util.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService implements IChatService {

    private final ChatMessageConverter chatMessageConverter;
    private final ChatMessageRepository chatMessageRepository;

    // Constructor injection
    public ChatService(ChatMessageConverter chatMessageConverter, ChatMessageRepository chatMessageRepository) {
        this.chatMessageConverter = chatMessageConverter;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        // Gán timestamp hiện tại
        message.setTimestamp(LocalDateTime.now());

        // Chuyển đổi từ DTO sang Entity
        ChatMessageEntity chatMessageEntity = chatMessageConverter.toEntity(message);

        // Lưu message vào cơ sở dữ liệu
        ChatMessageEntity savedMessage = chatMessageRepository.save(chatMessageEntity);

        // Chuẩn bị DTO trả về cho client
        return chatMessageConverter.toDTO(savedMessage);
    }

    // Caching chat history by chatId
    @Override
    @Cacheable(value = "chatHistory", key = "#chatId")
    public ChatMessageDTO getChatHistory(Long chatId) {
        ChatMessageEntity chatMessageEntity = chatMessageRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Chat message with ID %d not found", chatId)
                ));

        return chatMessageConverter.toDTO(chatMessageEntity);
    }
//
//    // Sử dụng Optional để trả về DTO nếu có
//    @Override
//    public Optional<ChatMessageDTO> getChatHistoryOptional(Long chatId) {
//        return chatMessageRepository.findById(chatId)
//                .map(chatMessageConverter::toDTO);
//    }

    // Lấy lịch sử chat giữa 2 người dùng (sender và receiver)
    @Override
    public List<ChatMessageDTO> getChatHistory(Long senderID, Long receiverID) {
        List<ChatMessageEntity> chats = chatMessageRepository.findBySenderIdAndReceiverId(senderID, receiverID);
        return chats.stream()
                .map(chatMessageConverter::toDTO)
                .collect(Collectors.toList());
    }

    // Lấy lịch sử chat giữa 2 người dùng trong một khoảng thời gian
    public List<ChatMessageDTO> getChatHistory(Long senderID, Long receiverID, LocalDateTime from, LocalDateTime to) {
        List<ChatMessageEntity> chats = chatMessageRepository
                .findBySenderIdAndReceiverIdAndTimestampBetween(senderID, receiverID, from, to);
        return chats.stream()
                .map(chatMessageConverter::toDTO)
                .collect(Collectors.toList());
    }
}
