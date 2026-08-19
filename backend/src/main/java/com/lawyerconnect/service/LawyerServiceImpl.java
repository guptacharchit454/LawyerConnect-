package com.lawyerconnect.service;

import com.lawyerconnect.model.Lawyer;
import com.lawyerconnect.repository.LawyerRepository;
import com.lawyerconnect.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LawyerServiceImpl implements LawyerService {

    private final LawyerRepository lawyerRepository;

    /**
     * Searches lawyers by specialization and fee bounds.
     * Cached in Redis using a compound key combining the search arguments.
     */
    @Override
    @Cacheable(value = "lawyer_searches", key = "#specialization + '-' + #minFee + '-' + #maxFee")
    @Transactional(readOnly = true)
    public List<Lawyer> searchLawyers(String specialization, BigDecimal minFee, BigDecimal maxFee) {
        return lawyerRepository.searchLawyers(specialization, minFee, maxFee);
    }

    /**
     * Creates a new lawyer profile.
     * Evicts all entries in the "lawyer_searches" cache to ensure directory integrity.
     */
    @Override
    @CacheEvict(value = "lawyer_searches", allEntries = true)
    @Transactional
    public Lawyer createLawyer(Lawyer lawyer) {
        lawyer.setId(null); // Ensure creation instead of replacement
        return lawyerRepository.save(lawyer);
    }

    /**
     * Updates an existing lawyer profile.
     * Evicts all entries in the "lawyer_searches" cache to prevent stale pricing display.
     */
    @Override
    @CacheEvict(value = "lawyer_searches", allEntries = true)
    @Transactional
    public Lawyer updateLawyer(UUID id, Lawyer updatedLawyer) {
        Lawyer existingLawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lawyer profile not found with ID: " + id));

        // Update fields
        existingLawyer.setName(updatedLawyer.getName());
        existingLawyer.setSpecialization(updatedLawyer.getSpecialization());
        existingLawyer.setHourlyRate(updatedLawyer.getHourlyRate());
        existingLawyer.setExperienceYears(updatedLawyer.getExperienceYears());
        existingLawyer.setEmail(updatedLawyer.getEmail());

        return lawyerRepository.save(existingLawyer);
    }

    /**
     * Retrieves an advocate profile. Does not query cache to ensure current state accuracy.
     */
    @Override
    @Transactional(readOnly = true)
    public Lawyer getLawyerById(UUID id) {
        return lawyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lawyer profile not found with ID: " + id));
    }
}
