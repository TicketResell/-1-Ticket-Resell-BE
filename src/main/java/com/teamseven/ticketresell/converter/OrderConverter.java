package com.teamseven.ticketresell.converter;
import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;
    // Chuyển đổi từ OrderEntity sang OrderDTO
    public OrderDTO toDTO(OrderEntity orderEntity) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderEntity.getId());
        orderDTO.setBuyerId(orderEntity.getBuyer().getId());
        orderDTO.setSellerId(orderEntity.getSeller().getId());
        orderDTO.setTicketId(orderEntity.getTicket().getId());
        orderDTO.setQuantity(orderEntity.getQuantity());
        orderDTO.setTotalAmount(orderEntity.getTotalAmount());
        orderDTO.setServiceFee(orderEntity.getServiceFee());
        orderDTO.setOrderMethod(orderEntity.getOrderMethod().name()); // Lấy tên enum
        orderDTO.setOrderStatus(orderEntity.getOrderStatus().name()); // Lấy tên enum
        orderDTO.setPaymentStatus(orderEntity.getPaymentStatus().name());
        return orderDTO;
    }

    // Chuyển đổi từ OrderDTO sang OrderEntity
    public OrderEntity toEntity(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setBuyer(userRepository.findById(orderDTO.getBuyerId()).orElse(null));  // Chuyển từ ID sang đối tượng UserEntity
        orderEntity.setSeller(userRepository.findById(orderDTO.getSellerId()).orElse(null)); // Chuyển từ ID sang đối tượng UserEntity
        orderEntity.setTicket(ticketRepository.findById(orderDTO.getTicketId()).orElse(null)); // Chuyển từ ID sang đối tượng TicketEntity
        orderEntity.setOrderMethod(OrderEntity.OrderMethod.valueOf(orderDTO.getOrderMethod())); // Chuyển từ String sang enum
        orderEntity.setQuantity(orderDTO.getQuantity());
        orderEntity.setTotalAmount(orderDTO.getTotalAmount());
        orderEntity.setServiceFee(orderDTO.getServiceFee());
        orderEntity.setOrderStatus(OrderEntity.OrderStatus.valueOf(orderDTO.getOrderStatus())); // Chuyển từ String sang enum
        orderEntity.setPaymentStatus(OrderEntity.PaymentStatus.valueOf(orderDTO.getPaymentStatus()));
        return orderEntity;
    }
}

