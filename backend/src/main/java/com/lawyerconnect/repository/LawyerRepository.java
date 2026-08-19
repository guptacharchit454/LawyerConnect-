package com.lawyerconnect.repository;

import com.lawyerconnect.model.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface LawyerRepository extends JpaRepository<Lawyer, UUID> {

    /**
     * Finds lawyers by their specialization and within an hourly fee range.
     * Uses Spring Data Method Name query generation.
     */
    List<Lawyer> findBySpecializationAndHourlyRateBetween(
            String specialization, 
            BigDecimal minFee, 
            BigDecimal maxFee
    );

    /**
     * Custom JPQL query for lawyer searches with explicit parameter mapping.
     */
    @Query("SELECT l FROM Lawyer l WHERE l.specialization = :specialization AND l.hourlyRate BETWEEN :minFee AND :maxFee")
    List<Lawyer> searchLawyers(
            @Param("specialization") String specialization, 
            @Param("minFee") BigDecimal minFee, 
            @Param("maxFee") BigDecimal maxFee
    );
}
