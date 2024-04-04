package nst.springboot.nstapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.service.AcademicTitleHistoryService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AcademicTitleHistoryController.class)
class AcademicTitleHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcademicTitleHistoryService academicTitleHistoryService;

    private ObjectMapper om;

    @Test
    @DisplayName("Test for saving an academic title history")
     void testSaveAcademicTitleHistory() throws Exception {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        // convert object to json
        AcademicTitleHistoryDto academicTitleHistoryDto = AcademicTitleHistoryDto.builder()
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
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .build();

        when(academicTitleHistoryService.save(any(AcademicTitleHistoryDto.class))).thenReturn(academicTitleHistoryDto);

        String body = om.writeValueAsString(academicTitleHistoryDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/academicTitleHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for fetching all academic title histories")
     void testGetAllAcademicTitleHistories() throws Exception {
        when(academicTitleHistoryService.getAll()).thenReturn(List.of(new AcademicTitleHistoryDto(), new AcademicTitleHistoryDto()));

        mockMvc.perform(MockMvcRequestBuilders.get("/academicTitleHistory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test for finding an academic title history by id")
     void testFindByIdAcademicTitleHistory() throws Exception {
        AcademicTitleHistoryDto academicTitleHistoryDto = AcademicTitleHistoryDto.builder()
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
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .build();
        when(academicTitleHistoryService.findById(1L)).thenReturn(academicTitleHistoryDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/academicTitleHistory/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Test for updating an academic title history")
     void testUpdateAcademicTitleHistory() throws Exception {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        AcademicTitleHistoryDto academicTitleHistoryDto = AcademicTitleHistoryDto.builder()
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
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .build();


        when(academicTitleHistoryService.update(1L, academicTitleHistoryDto)).thenReturn(academicTitleHistoryDto);

        String body = om.writeValueAsString(academicTitleHistoryDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/academicTitleHistory/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
