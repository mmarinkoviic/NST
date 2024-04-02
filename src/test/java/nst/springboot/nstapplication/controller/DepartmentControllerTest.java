package nst.springboot.nstapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.service.impl.DepartmentServiceImpl;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)

public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentServiceImpl departmentService;


    private DepartmentDto department;

    @BeforeEach
    public void setupTestData() {
        department = DepartmentDto.builder().id(1L).name("Katedra za informacione tehnologije").shortName("IS").build();
    }

    @Test
    @DisplayName("Test for saving an department")
    public void testSaveDepartment() throws Exception {

        when(departmentService.save(any(DepartmentDto.class))).thenReturn(department);

        mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(department)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Test for fetching all departments")
    public void testGetAllDepartments() throws Exception {
        DepartmentDto departmentDto = DepartmentDto.builder().name("Katedra za menadžment kvaliteta i standardizaciju").shortName("MNG").build();
        when(departmentService.getAll()).thenReturn(List.of(department,departmentDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/department")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Katedra za informacione tehnologije"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Katedra za menadžment kvaliteta i standardizaciju"));

    }

    @Test
    @DisplayName("Test for finding department by id")
    public void testFindByIdDepartment() throws Exception {
        department.setId(1L);
        when(departmentService.findById(1L)).thenReturn(department);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Katedra za informacione tehnologije"));

    }

    @Test
    @DisplayName("Test for deleting department")
    public void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Department removed!"));


    }

    @Test
    @DisplayName("Test for getting active secretary for department")
    public void testGetActiveSecretaryForDepartment() throws Exception {
        MemberDto memberDto = MemberDto.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .role(RoleDto.builder().name("Secretary").build())
                .build();

        when(departmentService.getActiveSecretaryForDepartment(1L)).thenReturn(memberDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/secretary", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value("Jelena"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value("Repac"));
    }

    @Test
    @DisplayName("Test for getting active head for department")
    public void testGetActiveHeadForDepartment() throws Exception {
        MemberDto memberDto = MemberDto.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .role(RoleDto.builder().name("Head").build())
                .build();

        when(departmentService.getActiveHeadForDepartment(1L)).thenReturn(memberDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/head", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value("Jelena"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value("Repac"));
    }

    @Test
    @DisplayName("Test for getting secretary history for department")
    public void testGetSecretaryHistoryForDepartment() throws Exception {
        SecretaryHistoryDto secretaryHistoryDto = SecretaryHistoryDto.builder()
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
                .build();;

        List<SecretaryHistoryDto> secretaryHistoryList = Collections.singletonList(secretaryHistoryDto);

        when(departmentService.getSecretaryHistoryForDepartment(1L)).thenReturn(secretaryHistoryList);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/secretary-history", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Test for getting head history for department")
    public void testGetHeadHistoryForDepartment() throws Exception {
        HeadHistoryDto headHistoryDto = HeadHistoryDto.builder()
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
                .build();;

        List<HeadHistoryDto> headHistoryDtoList = Collections.singletonList(headHistoryDto);

        when(departmentService.getHeadHistoryForDepartment(1L)).thenReturn(headHistoryDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/head-history", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Test for getting all members by department ID")
    public void testGetAllMembersByDepartmentId() throws Exception {
        List<MemberDto> memberDtoList = List.of(MemberDto.builder()
                        .id(1L)
                        .firstname("Jelena")
                        .lastname("Repac")
                        .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                        .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                        .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                        .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                        .role(RoleDto.builder().name("Default").build())
                        .build(),
                        MemberDto.builder()
                                .id(2L)
                                .firstname("Jovan")
                                .lastname("Ćirić")
                                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                                .role(RoleDto.builder().name("Secretary").build())
                                .build());
        when(departmentService.getAllMembersByDepartmentId(1L)).thenReturn(memberDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/member", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));

    }

    @Test
    @DisplayName("Test for updating a department")
    public void testUpdateDepartment() throws Exception {
        when(departmentService.update(1L, department)).thenReturn(department);
        mockMvc.perform(MockMvcRequestBuilders.put("/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(department)))
                .andExpect(MockMvcResultMatchers.status().isOk())
             ;
    }
    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
