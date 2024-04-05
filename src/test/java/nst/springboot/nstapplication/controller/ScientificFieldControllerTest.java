package nst.springboot.nstapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.nstapplication.dto.ScientificFieldDto;
import nst.springboot.nstapplication.service.ScientificFieldService;
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

@WebMvcTest(ScientificFieldController.class)
 class ScientificFieldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScientificFieldService scientificFieldService;

    @Test
    @DisplayName("Test for saving a scientific field")
     void testSaveScientificField() throws Exception {
        ScientificFieldDto scientificFieldDto = new ScientificFieldDto();
        scientificFieldDto.setName("Artificial intelligence");

        when(scientificFieldService.save(any(ScientificFieldDto.class))).thenReturn(scientificFieldDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/scientificField")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scientificFieldDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Test for fetching all scientific fields")
     void testGetAllScientificFields() throws Exception {
        ScientificFieldDto scientificFieldDto = ScientificFieldDto.builder().name("Artificial intelligence").build();
        ScientificFieldDto scientificFieldDto1 = ScientificFieldDto.builder().name("Scientific computing applications").build();

        when(scientificFieldService.getAll()).thenReturn(List.of(scientificFieldDto, scientificFieldDto1));

        mockMvc.perform(MockMvcRequestBuilders.get("/scientificField")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Artificial intelligence"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Scientific computing applications"));
    }

    @Test
    @DisplayName("Test for finding a scientific field by id")
     void testFindByIdScientificField() throws Exception {
        ScientificFieldDto scientificFieldDto = ScientificFieldDto.builder().id(1L).name("Artificial intelligence").build();

        when(scientificFieldService.findById(1L)).thenReturn(scientificFieldDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/scientificField/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Artificial intelligence"));
    }



    private Map<String, String> getUpdates() {
        Map<String, String> updates = new HashMap<>();
        updates.put("name", "Artificial intelligence");
        return updates;
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
