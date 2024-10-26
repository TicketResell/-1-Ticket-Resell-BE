package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.converter.RatingConverter;
import com.teamseven.ticketresell.converter.ReportConverter;
import com.teamseven.ticketresell.dto.RatingDTO;
import com.teamseven.ticketresell.dto.ReportDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.entity.ReportEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.RatingRepository;
import com.teamseven.ticketresell.repository.ReportRepository;
import com.teamseven.ticketresell.service.impl.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingReportController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RatingConverter ratingConverter;

    @Autowired
    private ReportConverter reportConverter;
    @Autowired
    private ReportRepository reportRepository;


    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody RatingDTO ratingDTO) {
        try {
            ratingDTO.setCreatedDate(LocalDateTime.now());
            RatingEntity ratingEntity = ratingConverter.toEntity(ratingDTO);
            RatingEntity savedRating = ratingRepository.save(ratingEntity);
            return ResponseEntity.ok(ratingConverter.toDTO(savedRating));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

//    // Tạo mới đánh giá
//    @PostMapping
//    public ResponseEntity<RatingEntity> createRating(@RequestBody RatingEntity rating) {
//        return ResponseEntity.ok(ratingService.createRating(rating));
//    }

    // Lấy đánh giá theo userId (seller)
    // Lấy đánh giá theo sellerId
    @PostMapping("/seller/{sellerId}")
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


    //create report
    @PostMapping("/create-report")
    public ResponseEntity<?> createReport(@RequestBody ReportDTO reportDTO) {
        try {
        ReportEntity reportEntity = reportConverter.toEntity(reportDTO);
        reportEntity.setCreatedDate(LocalDateTime.now());
        reportEntity.setStatus("pending");
        reportRepository.save(reportEntity);
        return ResponseEntity.ok("This report was post successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}

