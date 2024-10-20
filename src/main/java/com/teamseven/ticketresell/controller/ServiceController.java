package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.dto.JSONStorageDTO;
import com.teamseven.ticketresell.service.impl.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/json-storage")
public class ServiceController {

    @Autowired
    private JsonService jsonStorageService;

    // API to save JSON data
    @PostMapping("/save")
    public JSONStorageDTO saveJSONData(@RequestBody JSONStorageDTO dto) {
        return jsonStorageService.saveJSONData(dto);
    }

    // API to get JSON data by userId
    @GetMapping("/{userId}")
    public JSONStorageDTO getJSONData(@PathVariable Long userId) {
        return jsonStorageService.getJSONData(userId);
    }
}

