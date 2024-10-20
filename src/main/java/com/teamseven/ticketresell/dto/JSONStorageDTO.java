package com.teamseven.ticketresell.dto;

public class JSONStorageDTO {

    private Long userId;
    private String jsonData;

    // Constructors
    public JSONStorageDTO() {}

    public JSONStorageDTO(Long userId, String jsonData) {
        this.userId = userId;
        this.jsonData = jsonData;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}