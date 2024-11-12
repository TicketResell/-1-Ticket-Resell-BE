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
import java.util.Comparator;
import java.util.List;
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
        if (seller.isAgency()) {
            orderEntity.setServiceFee(0.05); // Nếu là agency, set phí dịch vụ thành 0.03
        } else {
            orderEntity.setServiceFee(0.1); // Nếu không phải agency, set phí dịch vụ thành 0.05
        }
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        // Trả về OrderDTO sau khi lưu
        return orderConverter.toDTO(savedOrder);
    }
    public List<OrderDTO> getOrdersBySeller(Long sellerId) {
        List<OrderEntity> orders = orderRepository.findBySeller_Id(sellerId);
        return orders.stream()
                .sorted(Comparator.comparing(OrderEntity::getCreatedDate).reversed())
                .map(orderConverter::toDTO)
                .collect(Collectors.toList());
    }
    public List<OrderDTO> getOrdersByBuyer(Long buyerId) {
        List<OrderEntity> orders = orderRepository.findByBuyer_Id(buyerId);
        return orders.stream()
                .sorted(Comparator.comparing(OrderEntity::getCreatedDate).reversed()) // Sắp xếp theo createdDate mới nhất
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
    public OrderDTO updatePaymentStatus(Long orderId, String paymentStatus, String vnpResponseCode, String vnpTransactionNo) {
        // Tìm order theo orderId
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng!"));

        // Kiểm tra nếu trạng thái payment được cập nhật thành "paid"
        if ("Paid".equalsIgnoreCase(paymentStatus)) {
            // Cập nhật trạng thái payment_status của order
            order.setPaymentStatus(OrderEntity.PaymentStatus.paid);

            // Kiểm tra phương thức thanh toán có phải VNPay không
            if (OrderEntity.OrderMethod.vnpay.equals(order.getOrderMethod())) {
                // Tạo transaction VNPay cho buyer
                transactionService.createBuyerTransactionWithVnpay(order, vnpResponseCode, vnpTransactionNo);
            } else {
                // Xử lý trường hợp không phải VNPay (các phương thức khác không cần tạo transaction VNPay)
                transactionService.createBuyerTransaction(order);
            }

        } else if ("Failed".equalsIgnoreCase(paymentStatus)) {
            // Nếu payment failed
            order.setPaymentStatus(OrderEntity.PaymentStatus.failed);
        } else {
            throw new IllegalArgumentException("Trạng thái thanh toán không hợp lệ.");
        }

        // Lưu order sau khi cập nhật payment status
        OrderEntity updatedOrder = orderRepository.save(order);

        // Trả về OrderDTO sau khi cập nhật
        return orderConverter.toDTO(updatedOrder);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, String orderStatus) {
        // Tìm order theo orderId
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found!"));
        // Kiểm tra nếu trạng thái đơn hàng được cập nhật thành "complete"
        if ("completed".equalsIgnoreCase(orderStatus)) {
            order.setOrderStatus(OrderEntity.OrderStatus.completed);
            transactionService.createSellerTransaction(order);
        } else if ("shipping".equalsIgnoreCase(orderStatus)) {
            // Xử lý khi trạng thái là "shipping"
            order.setOrderStatus(OrderEntity.OrderStatus.shipping);
        } else if ("received".equalsIgnoreCase(orderStatus)) {
            // Xử lý khi trạng thái là "received"
            order.setOrderStatus(OrderEntity.OrderStatus.received);
            if (!OrderEntity.PaymentStatus.paid.equals(order.getPaymentStatus())){
                order.setPaymentStatus(OrderEntity.PaymentStatus.paid);
                transactionService.createBuyerTransaction(order);
            }
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
            order.setOrderStatus(OrderEntity.OrderStatus.cancelled);
            // Kiểm tra nếu payment_status của order là "paid"
            TicketEntity ticket = order.getTicket();
            ticket.setQuantity(ticket.getQuantity() + order.getQuantity()); // Cộng lại số lượng vé
            // Nếu số lượng vé sau khi hoàn lại lớn hơn 0, đặt trạng thái thành "onsale"
            if (ticket.getQuantity() > 0) {
                ticket.setStatus("onsale");
            }
            ticketRepository.save(ticket);
            if (OrderEntity.PaymentStatus.paid.equals(order.getPaymentStatus())) {
                // Tạo transaction refund trả lại tiền cho buyer nếu đã thanh toán
                transactionService.createRefundTransaction(order);
            } else {
                // Không ném ngoại lệ mà trả về thông báo lỗi (tùy chọn cách xử lý)
                return orderConverter.toDTO(order);            }
        } else {
            throw new IllegalArgumentException("Invalid order status for refund");
        }

        // Lưu order sau khi cập nhật order status
        OrderEntity updatedOrder = orderRepository.save(order);

        // Trả về OrderDTO sau khi cập nhật
        return orderConverter.toDTO(updatedOrder);
    }

    public double getAllRevenue(){
       List<OrderEntity> orders = orderRepository.findAll();
       double revenue = 0;
       for (OrderEntity order : orders) {
           if (order.getOrderStatus().equals(OrderEntity.OrderStatus.completed)) {
               revenue = revenue + order.getTotalAmount();
           }
       }
       return revenue;
    }


    public double getAllProfit(){
        List<OrderEntity> orders = orderRepository.findAll();
        double profit = 0;
        for (OrderEntity order : orders) {
            if (order.getOrderStatus().equals(OrderEntity.OrderStatus.completed)) {
                profit = profit + order.getServiceFee()*order.getTotalAmount();
            }
        }
        return profit;
    }
}

