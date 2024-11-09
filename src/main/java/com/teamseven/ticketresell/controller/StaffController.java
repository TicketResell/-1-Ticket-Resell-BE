package com.teamseven.ticketresell.controller;


import com.teamseven.ticketresell.converter.OrderConverter;
import com.teamseven.ticketresell.converter.ReportConverter;
import com.teamseven.ticketresell.converter.UserConverter;
import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.dto.ReportDTO;
import com.teamseven.ticketresell.entity.*;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.RatingRepository;
import com.teamseven.ticketresell.repository.ReportRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.*;
import com.teamseven.ticketresell.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ReportRepository rrepository;

    @Autowired
    private ReportConverter reportConverter;

    // Lấy đánh giá theo order
    @GetMapping("/get-all-report")
    public ResponseEntity<?> getRatingsByOrderId(@PathVariable Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        return ResponseEntity.ok(ratingService.getRatingsByOrderId(orderId));
    }

    @GetMapping("/get-ban-user/{id}")
    public ResponseEntity<?> getBan(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            userService.banUser(id);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 NOT FOUND");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/get-number-of-user")
    public ResponseEntity<?> getNumberOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        int users = userService.countUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/total-ticket-sold")
    public ResponseEntity<?> getTotalTicketSold() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        int tickets = ticketService.totalTicketsSold();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/get-list-user")
    public ResponseEntity<?> getListUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        List<UserEntity> accounts = userRepository.findAll();
        return ResponseEntity.ok(accounts.stream().map(userConverter::toDTO).toList());
    }

    @GetMapping("/get-total-revenue-profit")
    public ResponseEntity<?> getTotalRevenueAndProfit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        double revenue = orderService.getAllRevenue();
        double profit = orderService.getAllProfit();


        Map<String, Double> response = new HashMap<>();
        response.put("revenue", revenue);
        response.put("profit", profit);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/view-chat-by-username")
    public ResponseEntity<?> viewChat2Users(@RequestParam Long user1, @RequestParam Long user2) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        List<ChatMessageDTO> chatMessages = chatService.getChatHistory(user1, user2);

        return ResponseEntity.ok(chatMessages);
    }

    @GetMapping("/view-all-report")
    public ResponseEntity<?> viewReport() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        List<ReportEntity> reportEntities = rrepository.findAll();
        return ResponseEntity.ok(reportEntities);
    }

        @GetMapping("/get-all-orders")
    public ResponseEntity<?> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        List< OrderEntity>  orderEntities = orderRepository.findAll();
        return ResponseEntity.ok(orderEntities.stream().map(orderConverter::toDTO).toList());
    }

    @PostMapping("/change-report-status/{id}")
    public ResponseEntity<?> changeReportStatus(@PathVariable Long id, @RequestParam String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ReportEntity reportEntity = rrepository.findById(id).orElse(null);
        reportEntity.setStatus(status);
        rrepository.save(reportEntity);

        return ResponseEntity.ok("Report with id " + id + " and status " + status);
    }
}
