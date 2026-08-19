package com.lawyerconnect.repository;

import com.lawyerconnect.model.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CaseRepository extends JpaRepository<Case, UUID> {

    /**
     * Finds all cases assigned to a specific lawyer.
     */
    List<Case> findByLawyerId(UUID lawyerId);

    /**
     * Finds all cases opened by a specific client name.
     */
    List<Case> findByClientName(String clientName);

    /**
     * Finds cases by status.
     */
    List<Case> findByStatus(String status);
}
