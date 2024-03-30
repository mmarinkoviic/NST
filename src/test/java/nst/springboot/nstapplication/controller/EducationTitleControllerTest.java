package nst.springboot.nstapplication.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.service.EducationTitleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(EducationTitleController.class)
public class EducationTitleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EducationTitleService educationTitleService;


    @Test
    @DisplayName("Test for saving an education title")
    public void testSaveEducationTitle() throws Exception {
        EducationTitleDto educationTitleDto = new EducationTitleDto();
        educationTitleDto.setName("Bachelor");

        when(educationTitleService.save(any(EducationTitleDto.class))).thenReturn(educationTitleDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/educationTitle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(educationTitleDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Test for fetching all education titles")
    public void testGetAllEducationTitles() throws Exception {
        EducationTitleDto educationTitleDto = EducationTitleDto.builder().name("Bachelor").build();
        EducationTitleDto educationTitleDto1 = EducationTitleDto.builder().name("Master").build();

        when(educationTitleService.getAll()).thenReturn(List.of(educationTitleDto, educationTitleDto1));

        mockMvc.perform(MockMvcRequestBuilders.get("/educationTitle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bachelor"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Master"));
    }

    @Test
    @DisplayName("Test for finding an education title by id")
    public void testFindByIdEducationTitle() throws Exception {
        EducationTitleDto educationTitleDto = EducationTitleDto.builder().id(1L).name("Bachelor").build();

        when(educationTitleService.findById(1L)).thenReturn(educationTitleDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/educationTitle/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bachelor"));
    }

    @Test
    @DisplayName("Test for partial updating an education title")
    public void testPartialUpdate() throws Exception {
        EducationTitleDto updatedEducationTitleDto = EducationTitleDto.builder().id(1L).name("Master").build();

        when(educationTitleService.partialUpdate(1L, getUpdates())).thenReturn(updatedEducationTitleDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/educationTitle/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Master\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Master"));
    }

    private Map<String, String> getUpdates() {
        Map<String, String> updates = new HashMap<>();
        updates.put("name", "Master");
        return updates;
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}