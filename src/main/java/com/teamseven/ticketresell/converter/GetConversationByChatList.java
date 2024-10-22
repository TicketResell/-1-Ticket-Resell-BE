package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.ConversationDTO;
import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetConversationByChatList {
    public ConversationDTO createConversation(List<ChatMessageEntity> chatMessageEntities) {
        Map<Long, Long> users = new HashMap<>();
        ConversationDTO conversationDTO = new ConversationDTO();

        int unreadCount = 0;

        for (ChatMessageEntity chatMessageEntity : chatMessageEntities) {
            if (!chatMessageEntity.getRead()) {
                unreadCount++;
            }
            users.put(chatMessageEntity.getUser1(), chatMessageEntity.getUser2());
            conversationDTO.setLastMessage(chatMessageEntity.getMessageContent());
        }
        conversationDTO.setUnreadCount(unreadCount);
        return conversationDTO;
    }
}
