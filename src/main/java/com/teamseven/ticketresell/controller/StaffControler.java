package com.teamseven.ticketresell.controller;


import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.repository.UserRepository;
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

   @Autowired
   private UserRepository  userRepository;


    // Lấy đánh giá theo order
    @GetMapping("/get-all-report")
    public ResponseEntity<List<RatingEntity>> getRatingsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(ratingService.getRatingsByOrderId(orderId));
    }

    @GetMapping("/get-ban-user/{id}")
    public ResponseEntity<Boolean> getBan(@PathVariable Long id) {
        userService.banUser(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/get-number-of-user")
    public ResponseEntity<Integer> getNumberOfUser() {
        int users = userService.countUser();
        return ResponseEntity.ok(users);
    }


}
