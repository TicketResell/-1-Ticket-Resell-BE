package com.teamseven.ticketresell.api;

import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.converter.TicketConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketAPI {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketConverter ticketConverter;

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
        return ResponseEntity.ok(tickets.stream().map(ticketConverter::toDTO).toList());
    }

//    // Get a ticket by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
//        Optional<TicketEntity> ticket = ticketRepository.findById(id);
//        return ticket.map(value -> ResponseEntity.ok(ticketConverter.toDTO(value)))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Update a ticket
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
//        Optional<TicketEntity> existingTicket = ticketRepository.findById(id);
//        if (existingTicket.isPresent()) {
//            TicketEntity updatedTicket = ticketConverter.toEntity(ticketDTO, existingTicket.get());
//            ticketRepository.save(updatedTicket);
//            return ResponseEntity.ok(ticketConverter.toDTO(updatedTicket));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    // Delete a ticket
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
//        Optional<TicketEntity> ticket = ticketRepository.findById(id);
//        if (ticket.isPresent()) {
//            ticketRepository.delete(ticket.get());
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
