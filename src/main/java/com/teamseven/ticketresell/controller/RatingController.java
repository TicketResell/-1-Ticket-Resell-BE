package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.converter.RatingConverter;
import com.teamseven.ticketresell.dto.RatingDTO;
import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.RatingRepository;
import com.teamseven.ticketresell.service.impl.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RatingConverter ratingConverter;


    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody RatingDTO ratingDTO) {
        RatingEntity ratingEntity = ratingConverter.toEntity(ratingDTO);
        ratingEntity.setCreatedDate(LocalDateTime.now());
        RatingEntity savedRating = ratingRepository.save(ratingEntity);
        return ResponseEntity.ok(ratingConverter.toDTO(savedRating));
    }
//    // Tạo mới đánh giá
//    @PostMapping
//    public ResponseEntity<RatingEntity> createRating(@RequestBody RatingEntity rating) {
//        return ResponseEntity.ok(ratingService.createRating(rating));
//    }

    // Lấy đánh giá theo userId (seller)
    // Lấy đánh giá theo sellerId
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getRatingsBySellerId(@PathVariable Long sellerId) {
        // Lấy danh sách các orders có sellerId đó
        List<OrderEntity> orders = orderRepository.findBySeller_Id(sellerId);
        if (orders != null && !orders.isEmpty()) {
            List<RatingEntity> ratings = new ArrayList<>();
            for (OrderEntity order : orders) {
                List<RatingEntity> orderRatings = ratingRepository.findByOrder_Id(order.getId());
                if (orderRatings != null) {
                    ratings.addAll(orderRatings);
                }
            }
            if (!ratings.isEmpty()) {
                return ResponseEntity.ok(ratings.stream().map(ratingConverter::toDTO).toList());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rating found for this seller!");
    }
    // Xóa đánh giá
    @DeleteMapping("/{rateId}")
    public ResponseEntity<?> deleteRating(@PathVariable Long rateId) {
        ratingService.deleteRating(rateId);
        return ResponseEntity.ok("This rating was deleted successfully.");
    }
}

