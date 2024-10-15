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


import java.util.List;
import java.util.Optional;

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
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        // Trả về OrderDTO sau khi lưu
        return orderConverter.toDTO(savedOrder);
    }
}
