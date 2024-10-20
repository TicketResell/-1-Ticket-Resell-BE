package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.JSONStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JSRepository extends  JpaRepository<JSONStorageEntity, Long>{
    JSONStorageEntity findById(int id);
}
