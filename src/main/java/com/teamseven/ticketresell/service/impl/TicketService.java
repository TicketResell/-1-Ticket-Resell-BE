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

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    public TicketEntity createTicket(TicketEntity ticket) {
        return ticketRepository.save(ticket);
    }

    public List<TicketEntity> findAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<TicketEntity> findTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public int totalTicketsSold(){
        List<TicketEntity> tickets = ticketRepository.findAll();
        int count = 0;
        for (TicketEntity ticket : tickets) {
            if(ticket.getStatus().equals("used")){
                count++;
            }
        }
        return count;
    }

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
            ticket.setImageUrls(ticketDetails.getImageUrls());
            ticket.setStatus(ticket.getStatus());
            // Lưu lại thay đổi
            return Optional.of(ticketRepository.save(ticket));
        } else {
            return Optional.empty();
        }
    }

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
    // Tìm vé theo categoryId và eventTitle
    public List<TicketEntity> searchTicketsByCategoryAndTitle(Long categoryId, String eventTitle) {
        return ticketRepository.findByCategoryIdAndEventTitleContainingIgnoreCase(categoryId, eventTitle);
    }
    public List<TicketEntity> searchDateAndTitle(LocalDate date, String eventTitle) {
        return ticketRepository.findByEventDateAfterAndEventTitleContainingIgnoreCaseOrderByEventDateAsc(date, eventTitle);
    }
    // Lấy danh sách vé theo salePrice tăng dần
    public List<TicketEntity> getTicketsSortedBySalePriceAsc() {
        return ticketRepository.findAllByOrderBySalePriceAsc();
    }

    // Lấy danh sách vé theo salePrice giảm dần
    public List<TicketEntity> getTicketsSortedBySalePriceDesc() {
        return ticketRepository.findAllByOrderBySalePriceDesc();
    }

    public String isReadyTicket(long id){
        TicketEntity ticket= findTicketById(id).orElse(null);
        if(ticket.getEventDate().isBefore(LocalDate.now())){
            ticket.setStatus("expired");
            return "Ticket is expired";
        }
        if(ticket.getQuantity() < 1){
            ticket.setStatus("used");
            return "Ticket is empty";
        }
        if(ticket == null)
            return "Ticket is null maybe it was deleted by seller";
        ticketRepository.save(ticket);
        return null;
    }

    public String showTicketName(long id){
        TicketEntity ticket= findTicketById(id).orElse(null);
        return ticket.getEventTitle();
    }
}
