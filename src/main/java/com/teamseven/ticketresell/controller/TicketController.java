package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.converter.TicketConverter;
import com.teamseven.ticketresell.service.impl.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketConverter ticketConverter;

    @Autowired
    private TicketService ticketService;
    // Create a new ticket
    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody TicketDTO ticketDTO) {
        ticketDTO.setStatus("onsale");
        TicketEntity ticketEntity = ticketConverter.toEntity(ticketDTO);
        ticketEntity.setCreatedDate(LocalDateTime.now());
        TicketEntity savedTicket = ticketRepository.save(ticketEntity);
        return ResponseEntity.ok(ticketConverter.toDTO(savedTicket));
    }

    // Get all tickets
    @GetMapping
    public ResponseEntity<?> getAllTickets() {
        List<TicketEntity> tickets = ticketRepository.findAll();
        List<TicketEntity> finalList = new ArrayList<>();
        for (TicketEntity ticket: tickets){
            if (ticket.getEventDate().isAfter(LocalDate.now())) {
                finalList.add(ticket);
            }else{
                ticket.setStatus("expired");
                ticketRepository.save(ticket);
            }
        }
        return ResponseEntity.ok(finalList);
    }
    // Lấy vé theo ID
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable long id) {

        TicketEntity existing = ticketRepository.findById(id);

        if (existing != null) {
            return ResponseEntity.ok(existing);
        }

        // if user not exist, return 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
    }

    // Cập nhật vé theo ID
    @PutMapping("/{id}")
    public ResponseEntity<TicketEntity> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        Optional<TicketEntity> updatedTicketOpt = ticketService.updateTicket(id, ticketDTO);

        // Trả về vé đã cập nhật nếu tìm thấy, nếu không trả về mã 404 Not Found
        return updatedTicketOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Xóa vé theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        boolean isDeleted = ticketService.deleteTicket(id);

        if (isDeleted) {
            // Trả về mã 200 OK cùng với thông báo nếu xóa thành công
            return ResponseEntity.ok("Ticket with id " + id + " was deleted successfully.");
        } else {
            // Trả về mã 404 Not Found cùng với thông báo nếu không tìm thấy vé để xóa
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ticket with id " + id + " was not found.");
        }
    }

    //find ticket by category ID
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getTicketsByCategory(@PathVariable Long categoryId) {
        List<TicketEntity> tickets = ticketRepository.findByCategoryId(categoryId);

        List<TicketEntity> result = new ArrayList<>();
        for (TicketEntity ticket: tickets){
            if(ticket.getEventDate().isAfter(LocalDate.now())){
                result.add(ticket);
            }
        }
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(result);
        }

        // Nếu không tìm thấy vé, trả về 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets found for category ID: " + categoryId);
    }
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getTicketsBySellerId(@PathVariable Long sellerId) {
        List<TicketEntity> tickets = ticketRepository.findBySeller_Id(sellerId);

        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        // Nếu không tìm thấy vé, trả về 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets found for this seller");
    }
    @GetMapping("/onsale-seller/{sellerId}")
    public ResponseEntity<?> getTicketsOnsaleBySellerId(@PathVariable Long sellerId) {
        List<TicketEntity> tickets = ticketRepository.findBySeller_IdAndStatus(sellerId,"onsale");
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        // Nếu không tìm thấy vé, trả về 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets found for this seller");
    }
    @PostMapping("/category-search")
    public ResponseEntity<?> getTicketsByCategorySearch(@RequestBody Map<String, Object> request) {
        // Lấy categoryId và eventTitle từ request body dưới dạng Map
        Long categoryId = Long.parseLong(request.get("categoryId").toString());
        String eventTitle = request.get("eventTitle").toString();

        // Tìm kiếm vé dựa trên categoryId và eventTitle
        List<TicketEntity> tickets = ticketRepository.findByCategoryIdAndEventTitleContainingIgnoreCase(categoryId, eventTitle);

        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }

        // Nếu không tìm thấy vé, trả về 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets found for this category search: " + eventTitle);
    }
    @PostMapping("/category-search-date")
    public ResponseEntity<?> getTicketsByCategorySearchDate(@RequestBody Map<String, Object> request) {
        // Lấy categoryId và eventTitle từ request body dưới dạng Map
        Long categoryId = Long.parseLong(request.get("categoryId").toString());
        String eventTitle = request.get("eventTitle").toString();

        // Tìm kiếm vé dựa trên categoryId và eventTitle
        List<TicketEntity> tickets = ticketRepository.findByCategoryIdAndEventTitleContainingIgnoreCase(categoryId, eventTitle);

        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }

        // Nếu không tìm thấy vé, trả về 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets found for this category search: " + eventTitle);
    }
    @PostMapping("/search-date")
    public ResponseEntity<?> getTicketsBySearchDate(@RequestBody Map<String, Object> request) {
        // Lấy categoryId và eventTitle từ request body dưới dạng Map
        // Lấy dữ liệu từ request body JSON
        LocalDate date = LocalDate.parse(request.get("date").toString());
        String eventTitle = request.get("eventTitle").toString();

        // Tìm kiếm vé dựa trên ngày và tiêu đề sự kiện
        List<TicketEntity> tickets = ticketService.searchDateAndTitle(date, eventTitle);
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets found for this date search: " + eventTitle);
    }

    @GetMapping("/used/{userId}")
    public ResponseEntity<?> getUsedTicketsByUserId(@PathVariable Long userId) {
        List<TicketEntity> tickets = ticketRepository.findBySeller_IdAndStatus(userId, "onsale");
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }

        // Nếu không tìm thấy vé, trả về 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No used tickets found for user ID: " + userId);
    }



    //show sold tickets by month + year
    @GetMapping("/sold/count/month/{month}/year/{year}")
    public ResponseEntity<List<TicketDTO>> getSoldTicketsCountByMonth(@PathVariable Integer month, @PathVariable Integer year) {
        List<TicketEntity> tickets = ticketRepository.findByStatus("used");
        List<TicketDTO> responseDTOs = new ArrayList<>();

        for (TicketEntity ticket : tickets) {
            LocalDate eventDate = ticket.getEventDate();
            if (eventDate != null && eventDate.getYear() == year && eventDate.getMonthValue() == month) {
                TicketDTO dto = new TicketDTO();
                dto.setId(ticket.getId());
                dto.setUserID(ticket.getSeller() != null ? ticket.getSeller().getId() : null); // Lấy userID từ seller
                dto.setPrice(ticket.getPrice());
                dto.setEventTitle(ticket.getEventTitle());
                dto.setEventDate(ticket.getEventDate());
                dto.setCategoryId(ticket.getCategory() != null ? ticket.getCategory().getId() : null); // Lấy categoryId
                dto.setLocation(ticket.getLocation());
                dto.setTicketType(ticket.getTicketType());
                dto.setSalePrice(ticket.getSalePrice());
                dto.setTicketDetails(ticket.getTicketDetails());
                dto.setImageUrls(ticket.getImageUrls());
                dto.setStatus(ticket.getStatus());
                responseDTOs.add(dto);
            }
        }

        if (!responseDTOs.isEmpty()) {
            return ResponseEntity.ok(responseDTOs);
        }

        // Nếu không tìm thấy vé, trả về danh sách rỗng với mã 204 (No Content)
        return ResponseEntity.noContent().build();
    }
    // API để tìm kiếm vé theo title hoặc ticket details, nhận query từ URL
    @GetMapping("/search/{term}")
    public ResponseEntity<?> searchTickets(@PathVariable("term") String term) {
        List<TicketEntity> tickets = ticketService.searchTickets(term);
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);  // Trả về message "empty ticket" nếu không tìm thấy vé
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ticket found for "+ term );  // Trả về danh sách vé tìm thấy
    }
    // API để lấy danh sách vé sắp hết hạn
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingTickets() {
        List<TicketEntity> tickets = ticketService.getUpcomingTickets();
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);  // Trả về message "empty ticket" nếu không tìm thấy vé
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tickets will come soon!" );  // Trả về danh sách vé tìm thấy
    }
    @GetMapping("/saleprice-asc")
    public ResponseEntity<?> getAscPriceTickets() {
        List<TicketEntity> tickets = ticketService.getTicketsSortedBySalePriceAsc();
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);  // Trả về message "empty ticket" nếu không tìm thấy vé
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tickets will come soon!" );  // Trả về danh sách vé tìm thấy
    }
    @GetMapping("/saleprice-desc")
    public ResponseEntity<?> getDescPriceTickets() {
        List<TicketEntity> tickets = ticketService.getTicketsSortedBySalePriceDesc();
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);  // Trả về message "empty ticket" nếu không tìm thấy vé
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tickets will come soon!" );  // Trả về danh sách vé tìm thấy
    }
    @GetMapping("/upcoming/{date}")// ví dụ kiểu 2024-10-10
    public ResponseEntity<?> getDateTickets(@PathVariable("date") LocalDate date) {
        List<TicketEntity> tickets = ticketService.getUpcomingTickets(date);
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);  // Trả về message "empty ticket" nếu không tìm thấy vé
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tickets will come soon!" );  // Trả về danh sách vé tìm thấy
    }
    // API phân trang với RequestBody không DTO
    @PostMapping("/checkValidTicket/{id}")
    public ResponseEntity<?> checkValidTicket(@PathVariable String id) {
        Long tickID;
        try {
            tickID = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ticket ID is not a number");
        }
        String status = ticketService.isReadyTicket(tickID);
        if(status == null){
            return ResponseEntity.ok("Ticket is ok");
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);

    }
}
