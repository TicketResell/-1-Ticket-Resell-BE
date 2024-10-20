package com.teamseven.ticketresell.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
//JSON STORAGE FOR JSON COMPARE IN FRONT-END
@Entity
@Table(name = "json_storage")
public class JSONStorageEntity extends BaseEntity {

    @Column(name = "userID", nullable = false)
    private Long userID ;

    @Column(name = "json_data", columnDefinition = "TEXT")
    private String jsonData;

    public JSONStorageEntity() {
    }

    public JSONStorageEntity(Long userID, String jsonData) {
        this.userID = userID;
        this.jsonData = jsonData;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
