package com.teamseven.ticketresell.controller;


import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.service.impl.RatingService;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffControler {

   @Autowired
   private RatingService ratingService;

   @Autowired
   private UserService userService;

    // Lấy đánh giá theo order
    @GetMapping("/get-all-report")
    public ResponseEntity<List<RatingEntity>> getRatingsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(ratingService.getRatingsByOrderId(orderId));
    }

    @GetMapping("/get-ban-user")
    public ResponseEntity<Boolean> getBan(@PathVariable Long orderId) {
        userService.getBanUser(orderId);
        return ResponseEntity.ok(true);
    }


}
