package com.teamseven.ticketresell.service;

import com.teamseven.ticketresell.dto.ChatMessageDTO;

import java.util.List;

public interface IChatService {
    ChatMessageDTO sendMessage(ChatMessageDTO message);
    List<ChatMessageDTO> getChatHistory(Long chatId);
    List<ChatMessageDTO> getChatHistory(Long buyerId, Long sellerId);

}
