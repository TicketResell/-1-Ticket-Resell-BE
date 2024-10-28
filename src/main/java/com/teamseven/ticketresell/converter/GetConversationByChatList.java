package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.ConversationDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetConversationByChatList {

    final
    UserService userService;

    public GetConversationByChatList(UserService userService) {
        this.userService = userService;
    }


    public ConversationDTO createConversation(List<ChatMessageEntity> chatMessageEntities) {
        ConversationDTO conversationDTO = new ConversationDTO();
        Map<Long, String> user1Map = new HashMap<>();
        Map<Long, String> user2Map = new HashMap<>();

        int unreadCount = 0;
        String lastMessage = "";

        for (ChatMessageEntity chatMessageEntity : chatMessageEntities) {
            // Kiểm tra số tin nhắn chưa đọc
            if (!chatMessageEntity.getRead()) {
                unreadCount++;
            }

            // Thêm user1 vào Map nếu chưa có
            if (!user1Map.containsKey(chatMessageEntity.getUser1())) {
                String user1FullName = userService.getFullNameByID(chatMessageEntity.getUser1());
                user1Map.put(chatMessageEntity.getUser1(), user1FullName);
            }

            // Thêm user2 vào Map nếu chưa có
            if (!user2Map.containsKey(chatMessageEntity.getUser2())) {
                String user2FullName = userService.getFullNameByID(chatMessageEntity.getUser2());
                user2Map.put(chatMessageEntity.getUser2(), user2FullName);
            }

            // Lưu nội dung tin nhắn cuối cùng
            lastMessage = chatMessageEntity.getMessageContent();
        }
        conversationDTO.setUser1(user1Map);
        conversationDTO.setUser2(user2Map);
        conversationDTO.setUnreadCount(unreadCount);
        conversationDTO.setLastMessage(lastMessage);

        return conversationDTO;
    }

}