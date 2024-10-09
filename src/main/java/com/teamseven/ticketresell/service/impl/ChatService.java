package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.repository.ChatMessageRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // Lưu tin nhắn vào cơ sở dữ liệu
    @Transactional
    public ChatMessageEntity saveMessage(Long buyerId, Long sellerId, Long senderId, String message) {
        ChatMessageEntity chatMessage = new ChatMessageEntity();
        chatMessage.setBuyerId(buyerId);
        chatMessage.setSellerId(sellerId);
        chatMessage.setSenderId(senderId);
        chatMessage.setMessage(message);
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    // Lấy lịch sử chat giữa buyer và seller
    public List<ChatMessageEntity> getChatHistory(Long buyerId, Long sellerId) {
        return chatMessageRepository.findByBuyerIdAndSellerIdOrderByTimestampAsc(buyerId, sellerId);
    }
}


