package com.teamseven.ticketresell.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {

    @Column(name = "categoryName", length = 255, nullable = false)
    private String categoryName;
    @OneToMany(mappedBy = "category")
    private List<TicketEntity> categories = new ArrayList<>();

    @Column(name = "description")
    private String description;

    // Getters and Setters

    public List<TicketEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<TicketEntity> categories) {
        this.categories = categories;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
