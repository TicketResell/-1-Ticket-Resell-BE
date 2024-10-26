package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.dto.OrderDTO;
import com.teamseven.ticketresell.entity.OrderEntity;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.OrderRepository;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.EmailService;
import com.teamseven.ticketresell.service.impl.OrderService;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderService orderService;

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
            if(order.getSendDeadline()!=null) {
                if (order.getRefundDeadline().isBefore(LocalDateTime.now())) {
                    if (!OrderEntity.PaymentStatus.paid.equals(order.getPaymentStatus())) {
                        order.setPaymentStatus(OrderEntity.PaymentStatus.paid);
                        transactionService.createBuyerTransaction(order);
                    }
                    order.setOrderStatus(OrderEntity.OrderStatus.completed);
                    transactionService.createSellerTransaction(order);
                }
            }
        }
    }

    @Scheduled(fixedRate = 3600000)//1 hour
    public void autoGetBanUser(){
        List<UserEntity> users = userRepository.findAll();
        for (UserEntity user : users) {
            if(user.getViolationWarning() > 2){
                user.setStatus("banned");
                userRepository.save(user);
                userRepository.flush();

                //Làm cái mail thông báo
                String sellerMail = user.getEmail();
                String subject = "Notice of Terms of Service Violation and Permanent Account Ban";
                String body = "Dear " + user.getEmail() + ",\n\n" +
                        "Following a thorough review, we have identified that your account has violated the Terms of Service of TicketResell. According to our policies, severe violations may lead to a permanent account ban.\n\n" +
                        "Due to these violations, your account has been permanently suspended and cannot be restored. You will no longer have access to the account or any associated services under this account.\n\n" +
                        "Thank you for using TicketResell, and we regret any inconvenience this may cause.\n\n" +
                        "Best regards,\n" +
                        "The TicketResell Team";
                emailService.sendEmail(sellerMail, subject, body);
            }
        }
    }

    public boolean orderConditions(OrderEntity order){
        if(order.getOrderMethod().equals(OrderEntity.OrderMethod.COD) && !order.getPaymentStatus().equals(OrderEntity.PaymentStatus.failed)) {
            return true;
        }
        if(order.getOrderMethod().equals(OrderEntity.OrderMethod.vnpay) && order.getPaymentStatus().equals(OrderEntity.PaymentStatus.paid)) {
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 3600000)//1 hour
    public void autoSetOrderStatusWhenSellerNotSendTicket(){
        List<OrderEntity> orders = orderRepository.findAll();
        for (OrderEntity order : orders) {
            if(order.getOrderStatus() == OrderEntity.OrderStatus.pending && order.getSendDeadline() ==null && orderConditions(order)) {
                order.setSendDeadline(LocalDateTime.now().plusDays(7));
                orderRepository.save(order);
                orderRepository.flush();
            }
            if(order.getSendDeadline()!=null) {
                if (order.getSendDeadline().isBefore(LocalDateTime.now()) && !order.getSellerWarn() && order.getOrderStatus().equals(OrderEntity.OrderStatus.pending)) { //chưa cảnh cáo
                    order.setOrderStatus(OrderEntity.OrderStatus.cancelled);
                    //Làm cái mail chửi tk bán
                    String sellerMail = order.getSeller().getEmail();
                    String subject = "Order Completion";
                    String body = "Dear " + order.getSeller().getFullname() + ",\n\n" +
                            "We noticed that you have not sent the ticket for your order in time. Therefore, we have canceled this order due to your non-compliance.\n\n" +
                            "Please be aware that repeated violations may lead to account suspension or get a permanent ban.\n\n" +
                            "If you believe this was a mistake, or if you need assistance, please contact our support team as soon as possible.\n\n" +
                            "Best regards,\n" +
                            "The TicketResell Team";
                    emailService.sendEmail(sellerMail, subject, body);
                    //cảnh cáo +1
                    order.setSellerWarn(true);
                    orderRepository.save(order);
                    orderRepository.flush();
                    try {
                        //câp nhật cảnh cáo vô database
                        UserEntity seller = userRepository.findById(order.getSeller().getId()).orElse(null);
                        seller.setViolationWarning((short) (seller.getViolationWarning() + 1));
                        userRepository.save(seller);
                        userRepository.flush();
                    }
                    catch (NullPointerException e) {
                        System.err.println("Error updating seller violation warning: " + e.getMessage());
                    }
                    //start refund
                    String body2 = "Dear " + order.getBuyer().getFullname() + ",\n\n" +
                            "We regret to inform you that your order has been canceled because the seller did not send the ticket on time.\n\n" +
                            "If payment has already been made, please rest assured that we are processing your refund, which will be credited back to your account as soon as possible.\n\n" +
                            "We apologize for the inconvenience this may have caused and appreciate your understanding.\n\n" +
                            "If you have any questions or need assistance, please do not hesitate to contact our support team.\n\n" +
                            "Best regards,\n" +
                            "The TicketResell Team";
                    emailService.sendEmail(order.getBuyer().getEmail(), subject, body2);

                    OrderDTO dto = orderService.updateOrderStatusForRefund(order.getId(),"cancelled");


                }
            }
        }
    }
    @Scheduled(fixedRate = 3600000) // 1 giờ (3600000 milliseconds)
    public void checkAndSetAgencyStatus() {
        List<UserEntity> sellers = userRepository.findAll();
        for (UserEntity seller : sellers) {
            // Đếm số đơn hàng "completed" của seller
            int completedOrders = orderRepository.countBySellerAndOrderStatus(seller, OrderEntity.OrderStatus.completed);
            if (completedOrders >= 15 && !seller.isAgency()) {
                // Nếu seller có đủ 15 đơn hàng và chưa là agency, cập nhật isAgency thành true
                seller.setAgency(true);
                userRepository.save(seller);
            }
        }
    }
}
