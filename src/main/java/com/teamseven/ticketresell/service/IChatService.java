package com.teamseven.ticketresell.service;

import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.dto.ConversationDTO;

import java.util.List;

public interface IChatService {
    ChatMessageDTO sendMessage(ChatMessageDTO message);
    List<ChatMessageDTO> getChatHistory(Long chatId);
    List<ChatMessageDTO> getChatHistory(Long buyerId, Long sellerId);
    public List<ConversationDTO> getConversationsByUserId(Long userId);
}
