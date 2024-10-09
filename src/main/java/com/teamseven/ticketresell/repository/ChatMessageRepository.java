//package com.teamseven.ticketresell.repository;
//
//import com.teamseven.ticketresell.entity.ChatMessageEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//
//
//public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
//    List<ChatMessageEntity> findByBuyerIdAndSellerIdOrderByTimestampAsc(Long buyerId, Long sellerId);
//    List<ChatMessageEntity> findByChatId(Long chatId);
//}
