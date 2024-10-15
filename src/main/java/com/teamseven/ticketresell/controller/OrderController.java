package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.converter.OrderConverter;
import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private OrderRepository orderRepository;
    // API tạo đơn hàng mới
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO createdOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(createdOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
    @GetMapping
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
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getOrdersBySeller(@PathVariable Long sellerId) {
        List<OrderDTO> orders = orderService.getOrdersBySeller(sellerId);
        if (orders != null) {
            return ResponseEntity.ok(orders);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Have not any sell order yet!");
    }
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<?> getOrdersByBuyer(@PathVariable Long buyerId) {
        List<OrderDTO> orders = orderService.getOrdersByBuyer(buyerId);
        if (orders != null) {
            return ResponseEntity.ok(orders);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Have not any buy order yet!");
    }
}

