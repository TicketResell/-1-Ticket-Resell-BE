package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatMessageConverter {

    // Chuyển từ ChatMessageEntity sang ChatMessageDTO
    public ChatMessageDTO toDTO(ChatMessageEntity chatMessageEntity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setChatId(chatMessageEntity.getChatId()); // Giả sử bạn có phương thức này trong entity
        dto.setSenderId(chatMessageEntity.getSenderId()); // Giả sử bạn có phương thức này trong entity
        dto.setMessage(chatMessageEntity.getMessage());
        dto.setTimestamp(chatMessageEntity.getTimestamp());
        return dto;
    }

    // Chuyển từ ChatMessageDTO sang ChatMessageEntity
    public ChatMessageEntity toEntity(ChatMessageDTO dto) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setSenderId(dto.getSenderId());
        chatMessageEntity.setBuyerId(dto.getRecipientId());
        chatMessageEntity.setMessage(dto.getMessage());
        chatMessageEntity.setTimestamp(LocalDateTime.now());
        return chatMessageEntity;
    }

}
