package com.teamseven.ticketresell.converter;
import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.TicketService;
import com.teamseven.ticketresell.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    // Chuyển đổi từ OrderEntity sang OrderDTO
    public OrderDTO toDTO(OrderEntity orderEntity) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderEntity.getId());
        orderDTO.setBuyerId(orderEntity.getBuyer().getId());
        orderDTO.setSellerId(orderEntity.getSeller().getId());
        orderDTO.setTicketId(orderEntity.getTicket().getId());
        orderDTO.setQuantity(orderEntity.getQuantity());
        orderDTO.setTotalAmount(orderEntity.getTotalAmount());
        orderDTO.setCreatedDate(orderEntity.getCreatedDate().toLocalDate());
        orderDTO.setServiceFee(orderEntity.getServiceFee());
        orderDTO.setOrderMethod(orderEntity.getOrderMethod().name()); // Lấy tên enum
        orderDTO.setOrderStatus(orderEntity.getOrderStatus().name()); // Lấy tên enum
        orderDTO.setPaymentStatus(orderEntity.getPaymentStatus().name());


        //thêm thông tin cho front-end:
        orderDTO.setBuyerName(userService.getFullNameByID(orderEntity.getBuyer().getId()));
        orderDTO.setSellerName(userService.getFullNameByID(orderEntity.getSeller().getId()));
        orderDTO.setTicketName(ticketService.showTicketName(orderEntity.getTicket().getId()));
        return orderDTO;
    }

    // Chuyển đổi từ OrderDTO sang OrderEntity
    public OrderEntity toEntity(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setBuyer(userRepository.findById(orderDTO.getBuyerId()).orElse(null));
        orderEntity.setSeller(userRepository.findById(orderDTO.getSellerId()).orElse(null));
        orderEntity.setTicket(ticketRepository.findById(orderDTO.getTicketId()).orElse(null));
        orderEntity.setOrderMethod(OrderEntity.OrderMethod.valueOf(orderDTO.getOrderMethod()));
        orderEntity.setQuantity(orderDTO.getQuantity());
        orderEntity.setTotalAmount(orderDTO.getTotalAmount());
        orderEntity.setServiceFee(orderDTO.getServiceFee());
        orderEntity.setOrderStatus(OrderEntity.OrderStatus.valueOf(orderDTO.getOrderStatus()));
        orderEntity.setPaymentStatus(OrderEntity.PaymentStatus.valueOf(orderDTO.getPaymentStatus()));
        if(orderEntity.getSellerWarn()==null) orderEntity.setSellerWarn(false);
        if(orderEntity.getBuyerWarn()==null) orderEntity.setBuyerWarn(false);

        return orderEntity;
    }
}

