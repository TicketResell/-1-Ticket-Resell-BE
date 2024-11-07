package com.teamseven.ticketresell.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class ReportEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "reported_id")
    private UserEntity reportedUser;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private UserEntity reporterUser;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private TicketEntity productId;
    @Column(name = "reason")
    private String reason;
    @Column(name = "status")
    private String status;

    // Getter và Setter

    public UserEntity getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(UserEntity reportedUser) {
        this.reportedUser = reportedUser;
    }

    public UserEntity getReporterUser() {
        return reporterUser;
    }

    public void setReporterUser(UserEntity reporterUser) {
        this.reporterUser = reporterUser;
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

    public TicketEntity getProductId() {
        return productId;
    }

    public void setProductId(TicketEntity productId) {
        this.productId = productId;
    }
}
