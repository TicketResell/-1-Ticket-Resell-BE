package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.converter.OrderConverter;
import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.service.impl.OrderService;
import com.teamseven.ticketresell.service.impl.TransactionService;
import com.teamseven.ticketresell.service.impl.UserService;
import com.teamseven.ticketresell.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class ShipCompletedController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderController orderController;
    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private TransactionService transactionService;
    @PutMapping("/set-shipping-status/{orderId}")
    public ResponseEntity<?> updateOrderStatusTrue(@PathVariable Long orderId,  @RequestBody Map<String, String> requestBody) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No authentication provided.");
        }

        String username = authentication.getName();
        String userRole = userService.getUserRoleByUsername(username);

        if (!userRole.equals("shipper")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Role - " + userRole);
        }

        String img = requestBody.get("img");
        if (img == null || img.isEmpty()) {
            return ResponseEntity.badRequest().body("Image URL is missing.");
        }

        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        if (orderEntity == null) {
            return ResponseEntity.badRequest().body("NOT FOUND FROM DB 404");
        }
        orderEntity.setImgShiper(img);
        orderEntity.setOrderStatus(OrderEntity.OrderStatus.received);
        if (!OrderEntity.PaymentStatus.paid.equals(orderEntity.getPaymentStatus())){
            orderEntity.setPaymentStatus(OrderEntity.PaymentStatus.paid);
            transactionService.createBuyerTransaction(orderEntity);
        }
        orderEntity.setRefundDeadline(LocalDateTime.now().plusDays(7));
        orderRepository.save(orderEntity);

        return ResponseEntity.ok(orderEntity);
    }
    @PutMapping("/set-shipping-status-false/{orderId}")
    public ResponseEntity<?> updateOrderStatusFalse(@PathVariable Long orderId, @RequestBody Map<String, String> requestBody) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No authentication provided.");
        }

        String username = authentication.getName();
        String userRole = userService.getUserRoleByUsername(username);

        if (!userRole.equals("shipper")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Role - " + userRole);
        }
        String img = requestBody.get("img");
        if (img == null || img.isEmpty()) {
            return ResponseEntity.badRequest().body("Image URL is missing.");
        }
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        if (orderEntity == null) {
            return ResponseEntity.badRequest().body("NOT FOUND FROM DB 404");
        }
        orderEntity.setImgShiper(img);
        orderEntity.setOrderStatus(OrderEntity.OrderStatus.orderbombing);
        if (OrderEntity.PaymentStatus.paid.equals(orderEntity.getPaymentStatus())){
            transactionService.createSellerTransaction(orderEntity);
        }
        orderRepository.save(orderEntity);
        orderRepository.flush();

        return ResponseEntity.ok(orderEntity);
    }
    @GetMapping("/all-order/ship")
    public ResponseEntity<?> allOrderForShipper() {
        List<OrderEntity> orders = orderRepository.findByOrderStatusIn(
                List.of(OrderEntity.OrderStatus.shipping, OrderEntity.OrderStatus.received,OrderEntity.OrderStatus.orderbombing)
        );
        return ResponseEntity.ok(orders);
    }

}