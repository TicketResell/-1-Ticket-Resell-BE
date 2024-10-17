package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.converter.OrderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Tìm Buyer
        UserEntity buyer = userRepository.findById(orderDTO.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Buyer ID"));
        // Tìm Seller
        UserEntity seller = userRepository.findById(orderDTO.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Seller ID"));
        // Tìm Ticket
        TicketEntity ticket = ticketRepository.findById(orderDTO.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Ticket ID"));
        // Kiểm tra trạng thái của vé có đang được bán hay không
        if (buyer.getId().equals(seller.getId())) {
            throw new IllegalArgumentException("You cannot purchase your own ticket.");
        }
        if (!"onsale".equalsIgnoreCase(ticket.getStatus())) {
            throw new IllegalArgumentException("Ticket is not available for sale.");
        }
        // Kiểm tra số lượng vé có đủ không
        if (ticket.getQuantity() < orderDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough tickets available.");
        }
        // Trừ số lượng vé và cập nhật trạng thái vé nếu hết vé
        ticket.setQuantity(ticket.getQuantity() - orderDTO.getQuantity());
        if (ticket.getQuantity() == 0) {
            ticket.setStatus("used");
        }
        ticketRepository.save(ticket); // Cập nhật vé sau khi trừ số lượng
        // Chuyển từ OrderDTO sang OrderEntity

        OrderEntity orderEntity = orderConverter.toEntity(orderDTO);
        // Lưu vào cơ sở dữ liệu
        orderEntity.setCreatedDate(LocalDateTime.now());
        orderEntity.setServiceFee(0.05);
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        // Trả về OrderDTO sau khi lưu
        return orderConverter.toDTO(savedOrder);
    }
    public List<OrderDTO> getOrdersBySeller(Long sellerId) {
        List<OrderEntity> orders = orderRepository.findBySeller_Id(sellerId);
        return orders.stream()
                .map(orderConverter::toDTO)
                .collect(Collectors.toList());
    }
    public List<OrderDTO> getOrdersByBuyer(Long buyerId) {
        List<OrderEntity> orders = orderRepository.findByBuyer_Id(buyerId);
        return orders.stream()
                .map(orderConverter::toDTO)
                .collect(Collectors.toList());
    }
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException("Order with ID " + orderId + " does not exist.");
        }
        orderRepository.deleteById(orderId);
    }
    @Transactional
    public OrderDTO updatePaymentStatus(Long orderId, String paymentStatus) {
        // Tìm order theo orderId
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found!"));
        // Kiểm tra nếu trạng thái payment được cập nhật thành paid
        if ("Paid".equalsIgnoreCase(paymentStatus)) {
            // Cập nhật trạng thái payment_status của order
            order.setPaymentStatus(OrderEntity.PaymentStatus.paid);

            // Tạo một transaction cho buyer (buyer đã trả tiền)
            transactionService.createBuyerTransaction(order);
        } else if ("Failed".equalsIgnoreCase(paymentStatus)) {
            // Nếu payment failed
            order.setPaymentStatus(OrderEntity.PaymentStatus.failed);
        } else {
            throw new IllegalArgumentException("Invalid payment status");
        }
        // Lưu order sau khi cập nhật payment status
        OrderEntity updatedOrder = orderRepository.save(order);
        // Chuyển đổi từ OrderEntity sang OrderDTO
        return orderConverter.toDTO(updatedOrder);
    }
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, String orderStatus) {
        // Tìm order theo orderId
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found!"));
        // Kiểm tra nếu trạng thái đơn hàng được cập nhật thành "complete"
        if ("Complete".equalsIgnoreCase(orderStatus)) {
            // Cập nhật trạng thái order_status của order
            order.setOrderStatus(OrderEntity.OrderStatus.completed);

            // Tạo một transaction expense cho seller và tính phí dịch vụ
            transactionService.createSellerTransaction(order);
        } else {
            throw new IllegalArgumentException("Invalid order status");
        }
        // Lưu order sau khi cập nhật order status
        OrderEntity updatedOrder = orderRepository.save(order);

        // Trả về OrderDTO sau khi cập nhật
        return orderConverter.toDTO(updatedOrder);
    }
    @Transactional
    public OrderDTO updateOrderStatusForRefund(Long orderId, String orderStatus) {
        // Tìm order theo orderId
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found!"));

        // Kiểm tra nếu trạng thái đơn hàng được cập nhật thành "cancelled"
        if ("cancelled".equalsIgnoreCase(orderStatus)) {
            // Kiểm tra nếu payment_status của order là "paid"
            if (OrderEntity.PaymentStatus.paid.equals(order.getPaymentStatus())) {
                // Cập nhật trạng thái order_status thành "cancelled"
                order.setOrderStatus(OrderEntity.OrderStatus.cancelled);

                // Tạo transaction refund trả lại tiền cho buyer
                transactionService.createRefundTransaction(order);
            } else {
                throw new IllegalArgumentException("Payment has not been completed. No refund necessary.");
            }
        } else {
            throw new IllegalArgumentException("Invalid order status for refund");
        }

        // Lưu order sau khi cập nhật order status
        OrderEntity updatedOrder = orderRepository.save(order);

        // Trả về OrderDTO sau khi cập nhật
        return orderConverter.toDTO(updatedOrder);
    }
}

