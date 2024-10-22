package com.teamseven.ticketresell.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class ReportEntity extends BaseEntity {

    @Column(name = "reported_UserId")
    private Long reportedUserId;
    @Column(name = "reporter_UserId")
    private Long reporterUserId;
    @Column(name = "reason")
    private String reason;
    @Column(name = "status")
    private String status;

    // Getter và Setter

    public Long getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Long reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public Long getReporterUserId() {
        return reporterUserId;
    }

    public void setReporterUserId(Long reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}