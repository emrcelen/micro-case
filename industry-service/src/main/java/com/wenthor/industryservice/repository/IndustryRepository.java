package com.wenthor.industryservice.repository;

import com.wenthor.industryservice.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, UUID> {
    Optional<Industry> findByName(String name);
    Optional<List<Industry>> findAllByIdIn(Iterable<UUID> ids);
}
