package com.wenthor.invitationservice.repository;

import com.wenthor.invitationservice.enumeration.Status;
import com.wenthor.invitationservice.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    Optional<Invitation> findByCode(String code);
    List<Invitation> findByUser(UUID user);
    List<Invitation> findAllByExpiredDateBeforeAndStatus(LocalDateTime currentDate, Status status);

}
