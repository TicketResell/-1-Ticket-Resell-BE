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

    @Autowired
    private UserRepository userRepository;

    // Chuyển đổi từ DTO sang Entity
    public ChatMessageEntity toEntity(ChatMessageDTO dto) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setUser1(userRepository.findById(dto.getUser1_id()).orElse(null));
        entity.setUser2(userRepository.findById(dto.getUser2_id()).orElse(null));
        entity.setMessageContent(dto.getMessageContent());
        entity.setMessageType(dto.getMessageType());
        entity.setCreatedDate(dto.getTimestamp());
        entity.setRead(dto.isIsread());
        return entity;
    }

    // Chuyển đổi từ Entity sang DTO
    public ChatMessageDTO toDTO(ChatMessageEntity entity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setChatId(entity.getId());
        dto.setUser1_id(entity.getUser1().getId());
        dto.setUser2_id(entity.getUser2().getId());
        dto.setMessageContent(entity.getMessageContent());
        dto.setUser2Name(userService.getFullNameByID(entity.getUser2().getId()));
        dto.setUser1_avatar(userService.getAvatarByID(entity.getUser1().getId()));
        dto.setTimestamp(entity.getTimestamp());
        dto.setMessageType(entity.getMessageType());
        dto.setIsread(entity.getRead());
        return dto;
    }
}
