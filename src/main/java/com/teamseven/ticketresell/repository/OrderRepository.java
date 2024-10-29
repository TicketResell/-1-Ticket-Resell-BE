package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây nếu cần
    List<OrderEntity> findBySeller_Id(Long sellerId);  // Tìm đơn hàng theo sellerId
    List<OrderEntity> findByBuyer_Id(Long buyerId);  // Tìm đơn hàng theo buyerId
    int countBySellerAndOrderStatus(UserEntity seller, OrderEntity.OrderStatus orderStatus);
    long countBySeller_Id(Long sellerId); // Đếm số lượng order của seller
    List<OrderEntity> findByOrderStatusAndOrderMethodAndPaymentStatus(OrderEntity.OrderStatus orderStatus, OrderEntity.OrderMethod orderMethod, OrderEntity.PaymentStatus paymentStatus);
    long countBySeller_IdAndOrderStatus(Long sellerId, OrderEntity.OrderStatus orderStatus);

}
