package com.teamseven.ticketresell.service;

import com.teamseven.ticketresell.dto.JSONStorageDTO;

public interface IJsonService {
    public JSONStorageDTO saveJSONData(JSONStorageDTO dto);
    public JSONStorageDTO getJSONData(Long userId);
}
