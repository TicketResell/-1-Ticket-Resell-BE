package com.teamseven.ticketresell.converter;

import com.teamseven.ticketresell.dto.CategoryDTO;
import com.teamseven.ticketresell.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryName(dto.getName());
        return entity;
    }

    public CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getCategoryName());
        return dto;
    }
}
