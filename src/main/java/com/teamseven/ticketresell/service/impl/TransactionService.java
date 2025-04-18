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
    private BalanceService balanceService;

    @Autowired
    private TicketRepository ticketRepository;
    // Tạo transaction INCOME cho buyer qua VNPay (tiền từ buyer tới sàn)
    public void createBuyerTransactionWithVnpay(OrderEntity order, String vnpResponseCode, String vnpTransactionNo) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setBuyer(order.getBuyer());
        transaction.setTransactionAmount(order.getTotalAmount());  // Số tiền buyer đã thanh toán
        transaction.setTransactionType(TransactionEntity.TransactionType.Income);
        transaction.setVnpResponseCode(vnpResponseCode);  // Mã phản hồi từ VNPay
        transaction.setVnpTransactionNo(vnpTransactionNo);  // Số giao dịch từ VNPay
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setSeller(order.getSeller());
        transaction.setOrder(order);
        transactionRepository.save(transaction);
    }
    // Tạo giao dịch cho buyer và seller
    public TransactionEntity createTransaction(OrderEntity order, double amount, TransactionEntity.TransactionType type) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setOrder(order);
        transaction.setBuyer(order.getBuyer());
        transaction.setSeller(order.getSeller());
        transaction.setTransactionAmount(amount);
        transaction.setTransactionType(type);
        transaction.setCreatedDate(LocalDateTime.now());
        if (TransactionEntity.TransactionType.Expense.equals(type)) {
            // Expense: System pays to seller
            balanceService.updateBalanceForUser(order.getSeller(), amount); // Add to seller's balance
        } else if (TransactionEntity.TransactionType.Refund.equals(type)) {
            // Refund: Return money to buyer in case of cancellation
            balanceService.updateBalanceForUser(order.getBuyer(), amount); // Add back to buyer's balance
        }
        return transactionRepository.save(transaction);
    }
    // Tạo giao dịch expense sàn trả tiền cho seller có tính serviefee -expense
    public TransactionEntity createSellerTransaction(OrderEntity order) {
        double serviceFee = order.getTotalAmount() * order.getServiceFee(); // Tính phí dịch vụ
        double sellerAmount = (order.getTotalAmount() - 15000)- serviceFee;
        return createTransaction(order, sellerAmount, TransactionEntity.TransactionType.Expense);
    }
    // Tạo giao dịch buyer trả tiền cho sàn - income
    public TransactionEntity createBuyerTransaction(OrderEntity order) {
        return createTransaction(order, order.getTotalAmount(), TransactionEntity.TransactionType.Income);
    }
    // Tạo giao dịch refund cho buyer khi hủy đơn hàng
    public TransactionEntity createRefundTransaction(OrderEntity order) {
        double refundAmount = order.getTotalAmount();
        refundAmount = refundAmount - 15000;// Hoàn trả toàn bộ số tiền đã thanh toán
        return createTransaction(order, refundAmount, TransactionEntity.TransactionType.Refund);
    }
}

