package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.service.impl.TicketService;
import com.teamseven.ticketresell.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTaskService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TransactionService transactionService;

//    // Tác vụ tự động khởi động
//    @Scheduled(fixedRate = 15000) //miliseconds
//    public void autoRefund() {
//        System.out.println("TEST AUTO OK");
//        System.out.println("TEST AUTO OK");
//        System.out.println("TEST AUTO OK");
//        System.out.println("TEST AUTO OK");
//        System.out.println("TEST AUTO OK");
//
//    }
    @Scheduled(fixedRate = 15000)
    public void autoSetTicketExpired(){
        List<TicketEntity> tickets = ticketRepository.findAll();
        for (TicketEntity ticket : tickets) {
            if(ticket.getEventDate().isBefore(LocalDate.now()) && ticket.getStatus().equals("onsale")) {
                ticket.setStatus("expired");
                System.out.println("FOUND 1 TICKET EXPIRED:" + ticket.getEventTitle());
                ticketRepository.save(ticket);
                ticketRepository.flush();
            }
            if(ticket.getQuantity()<1) {
                ticket.setStatus("used");
                ticketRepository.save(ticket);
                ticketRepository.flush();
            }

        }
    }

    @Scheduled(fixedRate = 3600000)//1 hour
    public void autoRefundTicket(){
        List<OrderEntity> orders = orderRepository.findAll();
        for (OrderEntity order : orders) {
            if(order.getRefundDeadline().isBefore(LocalDateTime.now())){
                if (!OrderEntity.PaymentStatus.paid.equals(order.getPaymentStatus())){
                    order.setPaymentStatus(OrderEntity.PaymentStatus.paid);
                    transactionService.createBuyerTransaction(order);
                }
                order.setOrderStatus(OrderEntity.OrderStatus.completed);
                transactionService.createSellerTransaction(order);
            }
        }
    }
}
