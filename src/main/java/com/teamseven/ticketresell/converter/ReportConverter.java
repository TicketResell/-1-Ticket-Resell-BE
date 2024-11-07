package com.teamseven.ticketresell.converter;


import com.teamseven.ticketresell.dto.ReportDTO;
import com.teamseven.ticketresell.entity.ReportEntity;
import com.teamseven.ticketresell.repository.TicketRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReportConverter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // Chuyển đổi từ DTO sang Entity
    public ReportEntity toEntity(ReportDTO dto) {
        ReportEntity entity = new ReportEntity();
        entity.setReportedUser(userRepository.findById(dto.getReportedUserId()).orElse(null));
        entity.setReporterUser(userRepository.findById(dto.getReporterUserId()).orElse(null));
        entity.setReason(dto.getReason());
        entity.setStatus(dto.getStatus());
        entity.setProductId(ticketRepository.findById(dto.getProductId()).orElse(null));
        return entity;
    }

    // Chuyển đổi từ Entity sang DTO
    public ReportDTO toDTO(ReportEntity entity) {
        ReportDTO dto = new ReportDTO();
        dto.setId(entity.getId());
        dto.setReportedUserId(entity.getReportedUser().getId());
        dto.setReporterUserId(entity.getReporterUser().getId());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus());
        dto.setReportDate(LocalDateTime.now());
        dto.setProductId(entity.getProductId().getId());
        return dto;
    }
}
