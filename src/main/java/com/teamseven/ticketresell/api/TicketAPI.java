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
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        boolean isDeleted = ticketService.deleteTicket(id);

        if (isDeleted) {
            // Trả về mã 204 No Content nếu xóa thành công
            return ResponseEntity.noContent().build();
        } else {
            // Trả về mã 404 Not Found nếu không tìm thấy vé để xóa
            return ResponseEntity.notFound().build();
        }
    }
}
