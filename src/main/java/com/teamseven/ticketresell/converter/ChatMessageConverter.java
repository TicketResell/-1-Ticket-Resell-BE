package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.ChatService;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageConverter {

    @Autowired
    UserService userService;

    // Chuyển đổi từ DTO sang Entity
    public ChatMessageEntity toEntity(ChatMessageDTO dto) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setUser1(dto.getUser1_id());
        entity.setUser2(dto.getUser2_id());
        entity.setMessageContent(dto.getMessageContent());
        entity.setMessageType(dto.getMessageType());
       entity.setCreatedDate(dto.getTimestamp());
        return entity;
    }

    // Chuyển đổi từ Entity sang DTO
    public ChatMessageDTO toDTO(ChatMessageEntity entity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setChatId(entity.getId());
        dto.setUser1_id(entity.getUser1());
        dto.setUser2_id(entity.getUser2());
        dto.setMessageContent(entity.getMessageContent());
        dto.setUser2Name(userService.getUser2FullName(entity.getUser2()));
        dto.setTimestamp(entity.getTimestamp());
        dto.setMessageType(entity.getMessageType());
        return dto;
    }
}
