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
        System.out.println("Found " + chatMessageEntities.size() + " messages for userId: " + userId); // Log số lượng tin nhắn tìm thấy


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
        // Lấy danh sách các tin nhắn của người dùng từ repository
        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findByUser1OrUser2(userId, userId);

        // Tạo danh sách để chứa các ConversationDTO
        Map<Long, ConversationDTO> conversationMap = new HashMap<>();

        for (ChatMessageEntity chatMessageEntity : chatMessageEntities) {
            Long otherUserId = chatMessageEntity.getUser1().equals(userId) ? chatMessageEntity.getUser2() : chatMessageEntity.getUser1();

            // Kiểm tra xem cuộc hội thoại đã tồn tại trong bản đồ chưa
            ConversationDTO conversationDTO = conversationMap.get(otherUserId);

            if (conversationDTO == null) {
                // Tạo một ConversationDTO mới nếu chưa có
                conversationDTO = new ConversationDTO();
                conversationDTO.setUsers(Map.of(userId, otherUserId)); // Lưu trữ người dùng trong cuộc hội thoại
                conversationDTO.setUnreadCount(0); // Khởi tạo số tin nhắn chưa đọc
                conversationMap.put(otherUserId, conversationDTO); // Thêm vào bản đồ
            }

            // Cập nhật nội dung tin nhắn cuối cùng
            conversationDTO.setLastMessage(chatMessageEntity.getMessageContent());

            // Cập nhật số lượng tin nhắn chưa đọc
            if (!chatMessageEntity.getRead()) {
                conversationDTO.setUnreadCount(conversationDTO.getUnreadCount() + 1);
            }
        }

        // Chuyển đổi từ Map sang List và trả về
        return new ArrayList<>(conversationMap.values());
    }

}
