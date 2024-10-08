package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    // Constructor injection
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Tạo mới vé
    public TicketEntity createTicket(TicketEntity ticket) {
        return ticketRepository.save(ticket);
    }

    // Lấy tất cả các vé
    public List<TicketEntity> findAllTickets() {
        return ticketRepository.findAll();
    }

    // Tìm vé theo ID, trả về Optional<TicketEntity>
    public Optional<TicketEntity> findTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    // Cập nhật thông tin vé
    public Optional<TicketEntity> updateTicket(Long id, TicketDTO ticketDetails) {
        Optional<TicketEntity> ticketOpt = findTicketById(id);

        if (ticketOpt.isPresent()) {
            TicketEntity ticket = ticketOpt.get();
            // Cập nhật thông tin vé
            ticket.setPrice(ticketDetails.getPrice());
            ticket.setEventTitle(ticketDetails.getEventTitle());
            ticket.setEventDate(ticketDetails.getEventDate());
            ticket.setLocation(ticketDetails.getLocation());
            ticket.setTicketType(ticketDetails.getTicketType());
            ticket.setSalePrice(ticketDetails.getSalePrice());
            ticket.setStatus(ticket.getStatus());
            // Lưu lại thay đổi
            return Optional.of(ticketRepository.save(ticket));
        } else {
            return Optional.empty();
        }
    }

    // Xóa vé
    public boolean deleteTicket(Long id) {
        Optional<TicketEntity> ticketOpt = findTicketById(id);

        if (ticketOpt.isPresent()) {
            ticketRepository.delete(ticketOpt.get());
            return true;
        } else {
            return false;
        }
    }
}
