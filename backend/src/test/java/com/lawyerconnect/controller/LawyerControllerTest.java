package com.lawyerconnect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyerconnect.config.SecurityConfig;
import com.lawyerconnect.model.Lawyer;
import com.lawyerconnect.service.LawyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LawyerController.class)
@Import(SecurityConfig.class)
public class LawyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawyerService lawyerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Lawyer lawyer;

    @BeforeEach
    void setUp() {
        lawyer = Lawyer.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .specialization("Civil")
                .hourlyRate(new BigDecimal("150.00"))
                .experienceYears(10)
                .email("johndoe@example.com")
                .build();
    }

    @Test
    void searchLawyers_ShouldReturnList() throws Exception {
        Mockito.when(lawyerService.searchLawyers(eq("Civil"), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(Collections.singletonList(lawyer));

        mockMvc.perform(get("/api/lawyers/search")
                        .param("specialization", "Civil")
                        .param("minFee", "100")
                        .param("maxFee", "200")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].specialization").value("Civil"));
    }

    @Test
    void createLawyer_ShouldReturnCreated() throws Exception {
        Mockito.when(lawyerService.createLawyer(any(Lawyer.class))).thenReturn(lawyer);

        // Remove ID for creation
        Lawyer newLawyer = Lawyer.builder()
                .name("John Doe")
                .specialization("Civil")
                .hourlyRate(new BigDecimal("150.00"))
                .experienceYears(10)
                .email("johndoe@example.com")
                .build();

        mockMvc.perform(post("/api/lawyers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newLawyer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }

    @Test
    void updateLawyer_ShouldReturnOk() throws Exception {
        UUID id = lawyer.getId();
        Mockito.when(lawyerService.updateLawyer(eq(id), any(Lawyer.class))).thenReturn(lawyer);

        mockMvc.perform(post("/api/lawyers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lawyer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
}
