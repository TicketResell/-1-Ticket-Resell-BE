package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findBySenderIdAndReceiverId(Long senderID, Long receiverID);


    @Query("SELECT c FROM ChatMessageEntity c WHERE c.senderId = :senderId")
    List<ChatMessageEntity> findBySenderId(@Param("senderId") Long senderId);
    @Query("SELECT c FROM ChatMessageEntity c WHERE c.senderId = :senderId OR c.receiverId = :receiverId")
    List<ChatMessageEntity> findBySenderIdOrReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
//    List<ChatMessageEntity> findBySenderIdAndReceiverIdAndTimestampBetween(Long senderID, Long receiverID, LocalDateTime from, LocalDateTime to);
}
