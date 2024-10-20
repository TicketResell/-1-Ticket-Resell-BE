package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.converter.JSONStorageConverter;
import com.teamseven.ticketresell.dto.JSONStorageDTO;
import com.teamseven.ticketresell.entity.JSONStorageEntity;
import com.teamseven.ticketresell.repository.JSRepository;
import com.teamseven.ticketresell.service.IJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonService implements IJsonService {

    @Autowired
    private JSRepository jsRepository;

    @Override
    public JSONStorageDTO saveJSONData(JSONStorageDTO dto) {
        JSONStorageEntity entity;

        if (dto.getUserId() != null) {
            entity = jsRepository.findById(dto.getUserId()).orElse(new JSONStorageEntity());
            entity.setUserID(dto.getUserId());
            entity.setJsonData(dto.getJsonData());
        } else {
            entity = JSONStorageConverter.toEntity(dto);
        }
        JSONStorageEntity savedEntity = jsRepository.save(entity);
        return JSONStorageConverter.toDTO(savedEntity);
    }

    @Override
    public JSONStorageDTO getJSONData(Long userId) {
        JSONStorageEntity entity = jsRepository.findById(userId).orElse(null);
        return JSONStorageConverter.toDTO(entity);
    }
}
