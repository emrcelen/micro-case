package com.wenthor.industryservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "industries")
public class Industry extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String normalizedName;
    @ManyToOne
    @JoinColumn(name = "parent_industry_id")
    private Industry parentIndustry;
    @OneToMany(mappedBy = "parentIndustry")
    private List<Industry> subIndustries = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "industry_users", joinColumns = @JoinColumn(name = "industry_id"))
    @Column(name = "user_id")
    private List<UUID> users = new ArrayList<>();

    public Industry() {
    }

    public Industry(Builder builder) {
        super(builder.id, LocalDateTime.now(), builder.updated, builder.createdBy, builder.updatedBy);
        this.name = builder.name;
        this.normalizedName = builder.normalizedName;
        this.parentIndustry = builder.parentIndustry;
        this.subIndustries = builder.subIndustries;
        this.users = builder.users;
    }

    // Getter:
    public String getName() {
        return name;
    }
    public String getNormalizedName() {
        return normalizedName;
    }
    public Industry getParentIndustry() {
        return parentIndustry;
    }
    public List<Industry> getSubIndustries() {
        return subIndustries;
    }
    public List<UUID> getUsers() {
        return users;
    }
    // Setter:
    public void setParentIndustry(Industry parentIndustry) {
        this.parentIndustry = parentIndustry;
    }

    public static class Builder {
        private UUID id = UUID.randomUUID();
        private LocalDateTime updated = LocalDateTime.now();
        private UUID createdBy;
        private UUID updatedBy;
        private String name;
        private String normalizedName;
        private Industry parentIndustry;
        private List<Industry> subIndustries = new ArrayList<Industry>();
        private List<UUID> users = new ArrayList<UUID>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder updatedDate(LocalDateTime dateTime) {
            this.updated = dateTime;
            return this;
        }

        public Builder createdBy(UUID id) {
            this.createdBy = id;
            return this;
        }

        public Builder updatedBy(UUID id) {
            this.updatedBy = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder normalizedName(String normalizedName) {
            this.normalizedName = normalizedName;
            return this;
        }

        public Builder parentIndustry(Industry industry) {
            this.parentIndustry = industry;
            return this;
        }

        public Builder subIndustries(List<Industry> industries) {
            this.subIndustries = industries;
            return this;
        }

        public Builder users(List<UUID> users) {
            this.users = users;
            return this;
        }

        public Industry build() {
            return new Industry(this);
        }
    }
}
