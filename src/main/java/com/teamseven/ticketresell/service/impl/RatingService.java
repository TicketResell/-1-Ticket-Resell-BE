package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<RatingEntity> getAllRatings() {
        return ratingRepository.findAll();
    }


    public List<RatingEntity> getRatingsByOrderId(Long orderId) {
        return ratingRepository.findByOrder_Id(orderId);
    }

    public RatingEntity createRating(RatingEntity rating) {
        rating.setCreatedDate(LocalDateTime.now());
        return ratingRepository.save(rating);
    }

    public RatingEntity updateRating(Long rateId, RatingEntity newRating) {
        return ratingRepository.findById(rateId)
                .map(rating -> {
                    rating.setRatingScore(newRating.getRatingScore());
                    rating.setFeedback(newRating.getFeedback());
                    rating.setCreatedDate(LocalDateTime.now());
                    return ratingRepository.save(rating);
                })
                .orElseThrow(() -> new RuntimeException("Rating not found!"));
    }

    public void deleteRating(Long rateId) {
        ratingRepository.deleteById(rateId);
    }
}

