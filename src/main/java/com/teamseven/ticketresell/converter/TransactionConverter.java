package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.TransactionDTO;
import com.teamseven.ticketresell.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverter {

    public TransactionEntity toEntity(TransactionDTO dto) {
        TransactionEntity entity = new TransactionEntity();

        entity.setTransactionAmount(dto.getTransactionAmount());
        entity.setCreatedDate(dto.getTransactionDate());
        entity.setTransactionType(TransactionEntity.TransactionType.valueOf(dto.getTransactionType()));
        return entity;
    }

    public TransactionDTO toDTO(TransactionEntity entity) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setTransactionDate(entity.getCreatedDate());
        dto.setTransactionType(entity.getTransactionType().name());
        dto.setOrderId(entity.getOrder().getId());
        dto.setBuyerId(entity.getBuyer().getId());
        dto.setSellerId(entity.getSeller().getId());
        return dto;
    }
}

