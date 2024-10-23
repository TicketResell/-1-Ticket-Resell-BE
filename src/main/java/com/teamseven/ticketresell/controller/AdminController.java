package com.teamseven.ticketresell.controller;


import com.teamseven.ticketresell.converter.OrderConverter;
import com.teamseven.ticketresell.converter.UserConverter;
import com.teamseven.ticketresell.dto.UserDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.RatingEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.OrderService;
import com.teamseven.ticketresell.service.impl.RatingService;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private UserConverter accountConverter;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private OrderRepository orderRepository;


    // Cho coi full account bao gồm admin
    @GetMapping("/view-accounts")
    public ResponseEntity<?> getAccounts() {
        List<UserEntity> accounts = userRepository.findAll();
        return ResponseEntity.ok(accounts.stream().map(accountConverter::toDTO).toList());
    }

    @PutMapping("/promote/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody Map<String, String> request) {
        try {
            String role = request.get("role");
            if (!"admin".equalsIgnoreCase(role) && !"staff".equalsIgnoreCase(role)) {
                return ResponseEntity.badRequest().body("Invalid role. Must be 'admin' or 'staff'.");
            }
            UserEntity user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
            user.setRole(role);
            userRepository.save(user);
            return ResponseEntity.ok("This user updated to "+ role +" successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @GetMapping("/count-orders")
    public ResponseEntity<?> countOrders() {
        try {
            long orderCount = orderRepository.count();
            return ResponseEntity.ok(orderCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/all-orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderEntity> orders = orderRepository.findAll();
            if (orders != null) {
                return ResponseEntity.ok(orders.stream().map(orderConverter::toDTO).toList());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Have not any order yet!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/get-number-of-user")
    public ResponseEntity<Integer> getNumberOfUser() {
        int users = userService.countUser();
        return ResponseEntity.ok(users);
    }
    @PutMapping("/update-service-fee/{orderId}")
    public ResponseEntity<?> updateServiceFee(@PathVariable Long orderId, @RequestBody Map<String, Double> request) {
        try {
            // Tìm Order theo orderId
            OrderEntity order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
            if (OrderEntity.OrderStatus.completed.equals(order.getOrderStatus())) {
                return ResponseEntity.badRequest().body("Cannot update service fee. Order is already complete.");
            }
            Double serviceFee = request.get("serviceFee");
            if (serviceFee == null || serviceFee <= 0) {
                return ResponseEntity.badRequest().body("Invalid service fee. Must be greater than 0.");
            }
            order.setServiceFee(serviceFee);
            orderRepository.save(order);
            return ResponseEntity.ok("Updated done!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
