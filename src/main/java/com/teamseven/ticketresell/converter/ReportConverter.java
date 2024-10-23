package com.teamseven.ticketresell.converter;


import com.teamseven.ticketresell.dto.ReportDTO;
import com.teamseven.ticketresell.entity.ReportEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReportConverter {

    // Chuyển đổi từ DTO sang Entity
    public ReportEntity toEntity(ReportDTO dto) {
        ReportEntity entity = new ReportEntity();
        entity.setReportedUserId(dto.getReportedUserId());
        entity.setReporterUserId(dto.getReporterUserId());
        entity.setReason(dto.getReason());
        entity.setStatus(dto.getStatus());
        entity.setProductId(dto.getProductId());
        return entity;
    }

    // Chuyển đổi từ Entity sang DTO
    public ReportDTO toDTO(ReportEntity entity) {
        ReportDTO dto = new ReportDTO();
        dto.setId(entity.getId());
        dto.setReportedUserId(entity.getReportedUserId());
        dto.setReporterUserId(entity.getReporterUserId());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus());
        dto.setReportDate(LocalDateTime.now());
        dto.setProductId(entity.getProductId());
        return dto;
    }
}
