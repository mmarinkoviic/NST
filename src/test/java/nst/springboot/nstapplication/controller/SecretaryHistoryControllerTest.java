package nst.springboot.nstapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.service.SecretaryHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(SecretaryHistoryController.class)
 class SecretaryHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecretaryHistoryService secretaryHistoryService;

    private SecretaryHistoryDto secretaryHistoryDto;
    private ObjectMapper om;

    @BeforeEach
    public void setUp() {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        secretaryHistoryDto = SecretaryHistoryDto.builder()
                .id(1L)
                .startDate(LocalDate.now())
                .member(MemberDto.builder()
                        .firstname("Jelena")
                        .lastname("Repac")
                        .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                        .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                        .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                        .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                        .role(RoleDto.builder().name("Default").build())
                        .build())
                .build();
    }



    @Test
    @DisplayName("Test for getting all secretary histories")
     void testGetAllSecretaryHistories() throws Exception {
        List<SecretaryHistoryDto> secretaryHistories = Collections.singletonList(secretaryHistoryDto);
        when(secretaryHistoryService.getAll()).thenReturn(secretaryHistories);

        mockMvc.perform(MockMvcRequestBuilders.get("/secretary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Test for finding a secretary history by ID")
     void testFindByIdSecretaryHistory() throws Exception {
        when(secretaryHistoryService.findById(1L)).thenReturn(secretaryHistoryDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/secretary/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Test for deleting a secretary history")
     void testDeleteSecretaryHistory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/secretary/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Secretary history removed!"));
    }

    @Test
    @DisplayName("Test for updating a secretary history")
     void testUpdateSecretaryHistory() throws Exception {
        when(secretaryHistoryService.patchSecretaryHistory(1L, secretaryHistoryDto)).thenReturn(secretaryHistoryDto);
        String body = om.writeValueAsString(secretaryHistoryDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/secretary/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
