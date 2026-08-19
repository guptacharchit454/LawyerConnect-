package com.lawyerconnect.service;

import com.lawyerconnect.model.Lawyer;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface LawyerService {

    /**
     * Search lawyers by specialization and hourly fee range.
     * Cached using Redis "lawyer_searches" cache name.
     */
    List<Lawyer> searchLawyers(String specialization, BigDecimal minFee, BigDecimal maxFee);

    /**
     * Create a new lawyer profile.
     * Evicts stale search cache entries.
     */
    Lawyer createLawyer(Lawyer lawyer);

    /**
     * Update an existing lawyer's profile and fees.
     * Evicts stale search cache entries.
     */
    Lawyer updateLawyer(UUID id, Lawyer updatedLawyer);

    /**
     * Fetch a lawyer profile by their unique ID.
     */
    Lawyer getLawyerById(UUID id);
}
