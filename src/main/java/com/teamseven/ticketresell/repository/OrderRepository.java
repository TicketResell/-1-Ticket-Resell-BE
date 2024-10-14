package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây nếu cần
}
