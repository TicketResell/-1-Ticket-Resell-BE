package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
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
    // search theo title va detail
    public List<TicketEntity> searchTickets(String searchTerm) {
        return ticketRepository.findByEventTitleContainingOrTicketDetailsContaining(searchTerm, searchTerm);
    }
    // Lấy danh sách vé sắp hết hạn dựa trên event_date
    public List<TicketEntity> getUpcomingTickets() {
        LocalDate today = LocalDate.now();  // Lấy ngày hiện tại
        return ticketRepository.findByEventDateAfterOrderByEventDateAsc(today);  // Trả về danh sách vé sắp xếp theo ngày gần nhất
    }
    public List<TicketEntity> getUpcomingTickets(LocalDate date) {
        return ticketRepository.findByEventDateAfterOrderByEventDateAsc(date);  // Trả về danh sách vé sắp xếp theo ngày gần nhất
    }
    public Page<TicketEntity> getTicketsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findAll(pageable);
    }
}
