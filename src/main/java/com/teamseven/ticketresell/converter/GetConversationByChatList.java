package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.ConversationDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetConversationByChatList {

    final UserService userService;

    public GetConversationByChatList(UserService userService) {
        this.userService = userService;
    }

    public ConversationDTO createConversation(List<ChatMessageEntity> chatMessageEntities) {
        ConversationDTO conversationDTO = new ConversationDTO();

        int unreadCount = 0;
        String lastMessage = "";
        Long user1 = null;
        Long user2 = null;
        String user1FullName = null;
        String user2FullName = null;

        for (ChatMessageEntity chatMessageEntity : chatMessageEntities) {
            // Kiểm tra số tin nhắn chưa đọc
            if (!chatMessageEntity.getRead()) {
                unreadCount++;
            }

            // Gán user1 và user2
            if (user1 == null) {
                user1 = chatMessageEntity.getUser1();
                user1FullName = userService.getFullNameByID(user1);
            }
            if (user2 == null) {
                user2 = chatMessageEntity.getUser2();
                user2FullName = userService.getFullNameByID(user2);
            }

            lastMessage = chatMessageEntity.getMessageContent();
        }

        conversationDTO.setUser1(user1);
        conversationDTO.setUser1FullName(user1FullName);
        conversationDTO.setUser2(user2);
        conversationDTO.setUser2FullName(user2FullName);
        conversationDTO.setUnreadCount(unreadCount);
        conversationDTO.setLastMessage(lastMessage);

        return conversationDTO;
    }
}
