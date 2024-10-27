package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.ConversationDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetConversationByChatList {

    private final UserService userService;

    public GetConversationByChatList(UserService userService) {
        this.userService = userService;
    }

    public ConversationDTO createConversation(List<ChatMessageEntity> chatMessageEntities) {
        List<Long> users = new ArrayList<>();
        ConversationDTO conversationDTO = new ConversationDTO();

        int unreadCount = 0;

        for (ChatMessageEntity chatMessageEntity : chatMessageEntities) {
            if (!chatMessageEntity.getRead()) {
                unreadCount++;
            }

            // Thêm user1 và user2 vào danh sách nếu chưa có
            if (!users.contains(chatMessageEntity.getUser1())) {
                users.add(chatMessageEntity.getUser1());
            }
            if (!users.contains(chatMessageEntity.getUser2())) {
                users.add(chatMessageEntity.getUser2());
            }

            conversationDTO.setLastMessage(chatMessageEntity.getMessageContent());
        }

        conversationDTO.setUsers(users);
        conversationDTO.setUnreadCount(unreadCount);
        conversationDTO.setUser2FullName(userService.getFullNameByID(users.getLast()));

        return conversationDTO;
    }
}