package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageConverter {

    // Chuyển đổi từ DTO sang Entity
    public ChatMessageEntity toEntity(ChatMessageDTO dto) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSenderId(dto.getSenderId());
        entity.setReceiverId(dto.getReceiverId());
        entity.setMessageContent(dto.getMessageContent());
        entity.setChatType(dto.getChatType());
       entity.setCreatedDate(dto.getTimestamp());
        return entity;
    }

    // Chuyển đổi từ Entity sang DTO
    public ChatMessageDTO toDTO(ChatMessageEntity entity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setChatId(entity.getChatId());
        dto.setSenderId(entity.getSenderId());
        dto.setReceiverId(entity.getReceiverId());
        dto.setMessageContent(entity.getMessageContent());
        dto.setTimestamp(entity.getTimestamp());
        dto.setChatType(entity.getChatType());
        return dto;
    }
}
