package com.lawyerconnect.repository;

import com.lawyerconnect.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Finds and orders all chat messages in a case chronologically.
     * Maps to the legalCase property in the Message entity.
     */
    List<Message> findByLegalCaseIdOrderByTimestampAsc(UUID caseId);
}
