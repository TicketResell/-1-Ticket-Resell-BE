package com.teamseven.ticketresell.converter;


import com.teamseven.ticketresell.dto.JSONStorageDTO;
import com.teamseven.ticketresell.entity.JSONStorageEntity;

public class JSONStorageConverter {

    // Convert from Entity to DTO
    public static JSONStorageDTO toDTO(JSONStorageEntity entity) {
        return new JSONStorageDTO(entity.getUserID(), entity.getJsonData());
    }

    // Convert from DTO to Entity
    public static JSONStorageEntity toEntity(JSONStorageDTO dto) {
        return new JSONStorageEntity(dto.getUserId(), dto.getJsonData());
    }
}
