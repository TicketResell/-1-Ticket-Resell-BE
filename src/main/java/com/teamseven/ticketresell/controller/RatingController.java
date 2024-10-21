package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.service.impl.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    // Lấy tất cả đánh giá
    @GetMapping
    public ResponseEntity<List<RatingEntity>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }



    // Lấy đánh giá theo order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<RatingEntity>> getRatingsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(ratingService.getRatingsByOrderId(orderId));
    }

    // Tạo mới đánh giá
    @PostMapping
    public ResponseEntity<RatingEntity> createRating(@RequestBody RatingEntity rating) {
        return ResponseEntity.ok(ratingService.createRating(rating));
    }

    // Cập nhật đánh giá
    @PutMapping("/{rateId}")
    public ResponseEntity<RatingEntity> updateRating(@PathVariable Long rateId, @RequestBody RatingEntity rating) {
        return ResponseEntity.ok(ratingService.updateRating(rateId, rating));
    }

    // Xóa đánh giá
    @DeleteMapping("/{rateId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long rateId) {
        ratingService.deleteRating(rateId);
        return ResponseEntity.noContent().build();
    }
}

