package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findBySenderIdAndReceiverId(Long senderID, Long receiverID);
    ChatMessageEntity findById(long id);
    List<ChatMessageEntity> findBySenderIdAndReceiverIdAndTimestampBetween(Long senderID, Long receiverID, LocalDateTime from, LocalDateTime to);
}
