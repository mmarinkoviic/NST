package nst.springboot.nstapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.service.HeadHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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

@WebMvcTest(HeadHistoryController.class)
public class HeadHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HeadHistoryService headHistoryService;

    private HeadHistoryDto headHistoryDto;
    private ObjectMapper om;

    @BeforeEach
    public void setUp() {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        headHistoryDto = HeadHistoryDto.builder()
                .id(1L)
                .startDate(LocalDate.now())
                .head(MemberDto.builder()
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
    @DisplayName("Test for saving a head history")
    public void testSaveHeadHistory() throws Exception {
        when(headHistoryService.save(any(HeadHistoryDto.class))).thenReturn(headHistoryDto);
        String body = om.writeValueAsString(headHistoryDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/head")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Test for getting all head histories")
    public void testGetAllHeadHistories() throws Exception {
        List<HeadHistoryDto> headHistories = Collections.singletonList(headHistoryDto);
        when(headHistoryService.getAll()).thenReturn(headHistories);

        mockMvc.perform(MockMvcRequestBuilders.get("/head")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Test for finding a head history by ID")
    public void testFindByIdHeadHistory() throws Exception {
        when(headHistoryService.findById(1L)).thenReturn(headHistoryDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/head/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Test for deleting a head history")
    public void testDeleteHeadHistory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/head/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Head history removed!"));
    }

    @Test
    @DisplayName("Test for updating a head history")
    public void testUpdateHeadHistory() throws Exception {
        when(headHistoryService.patchHeadHistory(1L, headHistoryDto)).thenReturn(headHistoryDto);
        String body = om.writeValueAsString(headHistoryDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/head/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
