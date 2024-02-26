package com.wenthor.industryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;
    private LocalDateTime updated;
    @Column(nullable = false, updatable = false)
    private UUID createdBy;
    private UUID updatedBy;

    public BaseEntity(){}

    public BaseEntity(UUID id, LocalDateTime created, LocalDateTime updated, UUID createdBy, UUID updatedBy) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    // Getter:
    public UUID getId() {
        return id;
    }
    public LocalDateTime getCreated() {
        return created;
    }
    public LocalDateTime getUpdated() {
        return updated;
    }
    public UUID getCreatedBy() {
        return createdBy;
    }
    public UUID getUpdatedBy() {
        return updatedBy;
    }

    // Setter:
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }
}
