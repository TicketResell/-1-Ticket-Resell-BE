package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.TicketDTO;
import com.teamseven.ticketresell.entity.CategoryEntity;
import com.teamseven.ticketresell.entity.TicketEntity;
import com.teamseven.ticketresell.repository.CategoryRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TicketConverter {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;


    // Convert DTO to Entity
    public TicketEntity toEntity(TicketDTO ticketDTO) {
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setPrice(ticketDTO.getPrice());
            ticketEntity.setSeller(userRepository.findById(ticketDTO.getUserID()).orElse(null));
        ticketEntity.setEventTitle(ticketDTO.getEventTitle());
        ticketEntity.setEventDate(ticketDTO.getEventDate());
        ticketEntity.setLocation(ticketDTO.getLocation());
        ticketEntity.setTicketType(ticketDTO.getTicketType());
        ticketEntity.setSalePrice(ticketDTO.getSalePrice());
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(ticketDTO.getCategoryId());
        if (categoryOptional.isPresent()) {
            CategoryEntity categoryEntity = categoryOptional.get();  // Lấy CategoryEntity từ Optional
            ticketEntity.setCategory(categoryEntity);
        }
//        ticketEntity.setCategory(categoryRepository.findById(ticketDTO.getCategoryId()));
        ticketEntity.setTicketDetails(ticketDTO.getTicketDetails());
        ticketEntity.setStatus(ticketDTO.getStatus());
        ticketEntity.setQuantity(ticketDTO.getQuantity());
        // Set foreign keys (seller and category) in actual application logic
        return ticketEntity;
    }

    // Convert Entity to DTO
    public TicketDTO toDTO(TicketEntity ticketEntity) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticketEntity.getId());
        ticketDTO.setUserID(ticketEntity.getSeller().getId());
        ticketDTO.setPrice(ticketEntity.getPrice());
        ticketDTO.setEventTitle(ticketEntity.getEventTitle());
        ticketDTO.setEventDate(ticketEntity.getEventDate());
        ticketDTO.setCategoryId(ticketEntity.getCategory().getId());
        ticketDTO.setLocation(ticketEntity.getLocation());
        ticketDTO.setTicketType(ticketEntity.getTicketType());
        ticketDTO.setSalePrice(ticketEntity.getSalePrice());
        ticketDTO.setTicketDetails(ticketEntity.getTicketDetails());
        ticketDTO.setImageUrls(ticketEntity.getImageUrls());
        ticketDTO.setStatus(ticketEntity.getStatus());
        ticketDTO.setQuantity(ticketEntity.getQuantity());
        return ticketDTO;
    }
}
