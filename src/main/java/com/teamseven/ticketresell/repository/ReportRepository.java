package com.teamseven.ticketresell.repository;


import com.teamseven.ticketresell.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

}
