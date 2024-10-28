package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.converter.ChatMessageConverter;
import com.teamseven.ticketresell.converter.GetConversationByChatList;
import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.dto.ConversationDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.repository.ChatMessageRepository;
import com.teamseven.ticketresell.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService implements IChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageConverter chatMessageConverter;

    @Autowired
    private GetConversationByChatList getConversationByChatList;

    @Autowired
    private UserService userService;


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

        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findByUser1OrUser2(userId, userId);
        System.out.println("Found " + chatMessageEntities.size() + " messages for userId: " + userId);


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
        List<ChatMessageEntity> chats = chatMessageRepository.findByUser1OrUser2(senderID, receiverID);
        return chats.stream()
                .map(chatMessageConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConversationDTO> getConversationsByUserId(Long userId) {
        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findByUser1OrUser2(userId, userId);
        List<ConversationDTO> conversationDTOS = new ArrayList<>();

        Map<Long, Integer> unreadCountMap = new HashMap<>();

        // Duyệt qua các tin nhắn và cập nhật DTO cho từng otherUserId
        for (ChatMessageEntity chatMessageEntity : chatMessageEntities) {
            Long otherUserId = chatMessageEntity.getUser1().equals(userId) ? chatMessageEntity.getUser2() : chatMessageEntity.getUser1();

            // Lấy hoặc khởi tạo ConversationDTO mới cho otherUserId
            ConversationDTO conversationDTO = conversationDTOS.stream()
                    .filter(dto -> dto.getUser2().containsKey(otherUserId) || dto.getUser1().containsKey(otherUserId))
                    .findFirst()
                    .orElse(new ConversationDTO());

            // Thiết lập user1 và user2 với thông tin (ID, fullName)
            Map<Long, String> user1Map = new HashMap<>();
            Map<Long, String> user2Map = new HashMap<>();
            user1Map.put(userId, userService.getFullNameByID(userId));
            user2Map.put(otherUserId, userService.getFullNameByID(otherUserId));

            conversationDTO.setUser1(user1Map);
            conversationDTO.setUser2(user2Map);

            // Cập nhật tin nhắn cuối cùng
            conversationDTO.setLastMessage(chatMessageEntity.getMessageContent());

            // Cập nhật số lượng tin nhắn chưa đọc
            int unreadCount = unreadCountMap.getOrDefault(otherUserId, 0);
            if (!chatMessageEntity.getRead()) {
                unreadCount++;
            }
            unreadCountMap.put(otherUserId, unreadCount);
            conversationDTO.setUnreadCount(unreadCount);

            // Thêm ConversationDTO vào danh sách nếu chưa tồn tại
            if (!conversationDTOS.contains(conversationDTO)) {
                conversationDTOS.add(conversationDTO);
            }
        }

        return conversationDTOS;
    }
}
