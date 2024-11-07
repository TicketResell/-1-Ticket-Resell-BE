package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByUser1IdOrUser2Id(Long user1, Long user2);


//    @Query("SELECT m FROM ChatMessageEntity m WHERE (m.user1 = :user1 AND m.user2 = :user2) OR (m.user1 = :user2 AND m.user2 = :user1)")
//    List<ChatMessageEntity> findByUser1AndUser2(long user1, long user2);

    @Query("SELECT m FROM ChatMessageEntity m WHERE (m.user1.id = :user1 AND m.user2.id = :user2) OR (m.user1.id = :user2 AND m.user2.id = :user1)")
    List<ChatMessageEntity> findByUser1AndUser2(@Param("user1") long user1, @Param("user2") long user2);
}
