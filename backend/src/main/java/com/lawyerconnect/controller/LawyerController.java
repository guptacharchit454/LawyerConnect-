package com.lawyerconnect.controller;

import com.lawyerconnect.model.Lawyer;
import com.lawyerconnect.service.LawyerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/lawyers")
@RequiredArgsConstructor
@Slf4j
public class LawyerController {

    private final LawyerService lawyerService;

    /**
     * Search lawyers based on specialization and fee bounds.
     * Invokes cached service method in LawyerServiceImpl.
     * GET /api/lawyers/search?specialization=Civil&minFee=0&maxFee=5000
     */
    @GetMapping("/search")
    public ResponseEntity<List<Lawyer>> searchLawyers(
            @RequestParam("specialization") String specialization,
            @RequestParam(value = "minFee", defaultValue = "0") BigDecimal minFee,
            @RequestParam(value = "maxFee", defaultValue = "1000000") BigDecimal maxFee
    ) {
        log.info("Received request to search lawyers for specialization: '{}' between fees: {} and {}", 
                specialization, minFee, maxFee);
        
        List<Lawyer> lawyers = lawyerService.searchLawyers(specialization, minFee, maxFee);
        return ResponseEntity.ok(lawyers);
    }

    /**
     * Creates a new profile or updates an existing one.
     * Triggers complete Redis cache eviction.
     * POST /api/lawyers
     */
    @PostMapping
    public ResponseEntity<Lawyer> createOrUpdateLawyer(@Valid @RequestBody Lawyer lawyer) {
        if (lawyer.getId() != null) {
            log.info("Received request to update existing lawyer profile with ID: '{}'", lawyer.getId());
            Lawyer updated = lawyerService.updateLawyer(lawyer.getId(), lawyer);
            return ResponseEntity.ok(updated);
        } else {
            log.info("Received request to create a new lawyer profile with email: '{}'", lawyer.getEmail());
            Lawyer created = lawyerService.createLawyer(lawyer);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        }
    }
}
