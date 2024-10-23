package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.RatingDTO;
import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatingConverter {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    // Chuyển từ RatingEntity sang RatingDTO
    public RatingDTO toDTO(RatingEntity ratingEntity) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(ratingEntity.getId());
        ratingDTO.setBuyerId(ratingEntity.getBuyer().getId()); // Lấy ID của buyer
        ratingDTO.setOrderId(ratingEntity.getOrder().getId()); // Lấy ID của order
        ratingDTO.setRatingScore(ratingEntity.getRatingScore());
        ratingDTO.setFeedback(ratingEntity.getFeedback());
        ratingDTO.setCreatedDate(ratingEntity.getCreatedDate());
        return ratingDTO;
    }

    // Chuyển từ RatingDTO sang RatingEntity
    public RatingEntity toEntity(RatingDTO ratingDTO) {
        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setBuyer(userRepository.findById(ratingDTO.getBuyerId()).orElse(null)); // Set đối tượng buyer
        ratingEntity.setOrder(orderRepository.findById(ratingDTO.getOrderId()).orElse(null)); // Set đối tượng order
        ratingEntity.setRatingScore(ratingDTO.getRatingScore());
        ratingEntity.setFeedback(ratingDTO.getFeedback());
        ratingEntity.setCreatedDate(ratingDTO.getCreatedDate());
        return ratingEntity;
    }
}

