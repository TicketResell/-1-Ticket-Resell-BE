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
import java.util.Map;

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
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Order with ID " + orderId + " has been deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
    // API để cập nhật payment status là paid thì tạo transaction income từ buyer đến sàn
    @PutMapping("/update-payment-status/{orderId}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long orderId, @RequestBody Map<String, String> request) {
        try {
            String paymentStatus = request.get("payment_status");
            OrderDTO updatedOrder = orderService.updatePaymentStatus(orderId, paymentStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
    // API để cập nhật order status thành complete và tạo transaction expense từ sàn cho seller
    @PutMapping("/update-order-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> request) {
        try {
            String orderStatus = request.get("order_status");
            OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, orderStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
    // API để cập nhật order status thành cancelled và tạo transaction refund cho buyer
    @PutMapping("/update-order-status-refund/{orderId}")
    public ResponseEntity<?> updateOrderStatusForRefund(@PathVariable Long orderId, @RequestBody Map<String, String> request) {
        try {
            String orderStatus = request.get("order_status");
            OrderDTO updatedOrder = orderService.updateOrderStatusForRefund(orderId, orderStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}

