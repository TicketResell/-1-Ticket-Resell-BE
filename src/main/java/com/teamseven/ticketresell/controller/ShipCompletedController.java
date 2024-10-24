package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.service.impl.OrderService;
import com.teamseven.ticketresell.service.impl.UserService;
import com.teamseven.ticketresell.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PutMapping("/set-shipping-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId) {

        //giả dụ, ta call postman API trên thay vì đơn vị giao hàng thông báo thành công
        //như vậy, ta dùng xác thực role staff làm thay thế tạm


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xác thực
        if (authentication == null || !authentication.isAuthenticated() || userService.getUserRoleByUsername(authentication.getName()).equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);

        if (orderEntity == null) {
            return ResponseEntity.badRequest().body("NOT FOUND FROM DB 404");
        }
        orderEntity.setOrderStatus(OrderEntity.OrderStatus.received);
        orderEntity.setRefundDeadline(LocalDateTime.now().plusDays(7));
        orderRepository.save(orderEntity);

        return ResponseEntity.ok().build();
    }


}