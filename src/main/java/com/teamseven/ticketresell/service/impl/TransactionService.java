package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.TransactionEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.repository.TransactionRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;
    // Tạo giao dịch cho buyer và seller
    public TransactionEntity createTransaction(OrderEntity order, double amount, TransactionEntity.TransactionType type) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setOrder(order);
        transaction.setBuyer(order.getBuyer());
        transaction.setSeller(order.getSeller());
        transaction.setTransactionAmount(amount);
        transaction.setTransactionType(type);
        transaction.setCreatedDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
    // Tạo giao dịch expense sàn trả tiền cho seller có tính serviefee -expense
    public TransactionEntity createSellerTransaction(OrderEntity order) {
        double serviceFee = order.getTotalAmount() * order.getServiceFee(); // Tính phí dịch vụ
        double sellerAmount = order.getTotalAmount() - serviceFee;
        return createTransaction(order, sellerAmount, TransactionEntity.TransactionType.Expense);
    }
    // Tạo giao dịch buyer trả tiền cho sàn - income
    public TransactionEntity createBuyerTransaction(OrderEntity order) {
        return createTransaction(order, order.getTotalAmount(), TransactionEntity.TransactionType.Income);
    }
    // Tạo giao dịch refund cho buyer khi hủy đơn hàng
    public TransactionEntity createRefundTransaction(OrderEntity order) {
        double refundAmount = order.getTotalAmount(); // Hoàn trả toàn bộ số tiền đã thanh toán
        return createTransaction(order, refundAmount, TransactionEntity.TransactionType.Refund);
    }
}

