package nst.springboot.nstapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.service.AcademicTitleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AcademicTitleController.class)
 class AcademicTitleControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcademicTitleService academicTitleService;

    @Test
    @DisplayName("Test for saving an academic title")
     void testSaveAcademicTitle() throws Exception {

        AcademicTitleDto academicTitleDto = new AcademicTitleDto();
        academicTitleDto.setName("Professor");

        when(academicTitleService.save(any(AcademicTitleDto.class))).thenReturn(academicTitleDto);

        mockMvc.perform(post("/academicTitle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(academicTitleDto)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Test for fetching all academic titles")
     void testGetAllAcademicTitle() throws Exception {
        AcademicTitleDto academicTitleDto= AcademicTitleDto.builder().name("Professor").build();
        AcademicTitleDto academicTitleDto1= AcademicTitleDto.builder().name("Assistant").build();

        when(academicTitleService.getAll()).thenReturn(List.of(academicTitleDto,academicTitleDto1));

        mockMvc.perform(MockMvcRequestBuilders.get("/academicTitle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Professor"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Assistant"));

    }

    @Test
    @DisplayName("Test for finding academic title by id")
     void testFindByIdAcademicTitle() throws Exception {
        AcademicTitleDto academicTitleDto= AcademicTitleDto.builder().id(1L).name("Professor").build();
        when(academicTitleService.findById(1L)).thenReturn(academicTitleDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/academicTitle/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Professor"));

    }
    @Test
    @DisplayName("Test for partial updating academic title")
     void testPartialUpdate() throws Exception {
        AcademicTitleDto updatedAcademicTitleDto= AcademicTitleDto.builder().id(1L).name("Professor").build();
        when(academicTitleService.partialUpdate(1L, getUpdates())).thenReturn(updatedAcademicTitleDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/academicTitle/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Professor\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Professor"));
    }

    private Map<String, String> getUpdates() {
        Map<String, String> updates = new HashMap<>();
        updates.put("name", "Professor");
        return updates;
    }
    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
