package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.converter.OrderConverter;
import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.UserEntity;
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

    @PutMapping("/set-shipping-status/{orderId}")
    public ResponseEntity<?> updateOrderStatusTrue(@PathVariable Long orderId, String img) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No authentication provided.");
        }

        String username = authentication.getName();
        String userRole = userService.getUserRoleByUsername(username);

        if (!userRole.equals("shipper")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Role - " + userRole);
        }
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);

        if (orderEntity == null) {
            return ResponseEntity.badRequest().body("NOT FOUND FROM DB 404");
        }
        orderEntity.setImgShiper(img);
        orderEntity.setOrderStatus(OrderEntity.OrderStatus.received);
        orderEntity.setPaymentStatus(OrderEntity.PaymentStatus.paid);
        orderEntity.setRefundDeadline(LocalDateTime.now().plusDays(7));
        orderRepository.save(orderEntity);

        return ResponseEntity.ok().build();
    }


    @PutMapping("/set-shipping-status-false/{orderId}")
    public ResponseEntity<?> updateOrderStatusFalse(@PathVariable Long orderId, String img) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No authentication provided.");
        }

        String username = authentication.getName();
        String userRole = userService.getUserRoleByUsername(username);

        if (!userRole.equals("shipper")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Role - " + userRole);
        }

        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);

        if (orderEntity == null) {
            return ResponseEntity.badRequest().body("NOT FOUND FROM DB 404");
        }
        orderEntity.setImgShiper(img);
        orderEntity.setOrderStatus(OrderEntity.OrderStatus.orderbombing);
        orderRepository.save(orderEntity);
        orderRepository.flush();

        return ResponseEntity.ok().build();
    }


    @GetMapping("/show-all-order/ship")
    public ResponseEntity<?> showAllOrderForShip() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No authentication provided.");
        }

        String username = authentication.getName();
        String userRole = userService.getUserRoleByUsername(username);

        if (!userRole.equals("shipper")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Role - " + userRole);
        }

        // Lấy tất cả các đơn hàng có trạng thái và phương thức thanh toán mong muốn
        List<OrderEntity> orders = orderRepository.findByOrderStatus(
                OrderEntity.OrderStatus.shipping
        );
        List<OrderDTO> dtos = new ArrayList<>();
        for (OrderEntity order : orders) {
            OrderDTO dto = orderConverter.toDTO(order);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

}