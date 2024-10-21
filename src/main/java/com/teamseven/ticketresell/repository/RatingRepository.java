package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    RatingEntity findById(long id);

    List<RatingEntity> findByBuyer(UserEntity buyer);
    List<RatingEntity> findByOrder_Id(Long orderId);
}
