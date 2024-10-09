package com.teamseven.ticketresell.api;

import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.converter.TicketConverter;
import com.teamseven.ticketresell.service.impl.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketAPI {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketConverter ticketConverter;

    @Autowired
    private TicketService ticketService;
    // Create a new ticket
    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody TicketDTO ticketDTO) {
        TicketEntity ticketEntity = ticketConverter.toEntity(ticketDTO);
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
        return ResponseEntity.ok(finalList.stream().map(ticketConverter::toDTO).toList());
    }
    // Lấy vé theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable long id) {

        TicketEntity existing = ticketRepository.findById(id);

        if (existing != null) {
            return ResponseEntity.ok(ticketConverter.toDTO(existing));
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
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets.stream().map(ticketConverter::toDTO).toList());
        }

        // Nếu không tìm thấy vé, trả về 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets found for category ID: " + categoryId);
    }


    @GetMapping("/used/{userId}")
    public ResponseEntity<?> getUsedTicketsByUserId(@PathVariable Long userId) {
        List<TicketEntity> tickets = ticketRepository.findBySeller_IdAndStatus(userId, "used");
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets.stream().map(ticketConverter::toDTO).toList());
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



}
