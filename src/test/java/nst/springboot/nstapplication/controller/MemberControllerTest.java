package nst.springboot.nstapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.service.impl.DepartmentServiceImpl;
import nst.springboot.nstapplication.service.impl.MemberServiceImpl;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)

public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberServiceImpl memberService;


    private MemberDto memberDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setupTestData() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        memberDto = MemberDto.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .role(RoleDto.builder().name("Default").build())
                .build();
    }

    @Test
    @DisplayName("Test for saving an member")
    public void testSaveMember() throws Exception {

        when(memberService.save(any(MemberDto.class))).thenReturn(memberDto);

        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDto)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Test for fetching all members")
    public void testGetAllMembers() throws Exception {
        MemberDto memberDto1= MemberDto.builder()
                .firstname("Jovan")
                .lastname("Ćirić")
                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .role(RoleDto.builder().name("Head").build())
                .build();
        when(memberService.getAll()).thenReturn(List.of(memberDto, memberDto1));

        mockMvc.perform(MockMvcRequestBuilders.get("/member")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname").value("Jelena"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstname").value("Jovan"));


    }

    @Test
    @DisplayName("Test for finding member by id")
    public void testFindByIdMember() throws Exception {
        memberDto.setId(1L);
        when(memberService.findById(1L)).thenReturn(memberDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/member/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value("Jelena"));

    }

    @Test
    @DisplayName("Test for deleting member")
    public void testDeleteMember() throws Exception {
        doNothing().when(memberService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/member/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Member removed!"));


    }

    @Test
    @DisplayName("Test for updating a member")
    public void testUpdateMember() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Ciric")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();

        MemberDto updatedMemberDto = MemberDto.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Ciric")
                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .role(RoleDto.builder().name("Default").build())
                .build();

        when(memberService.patchUpdateMember(1L, member)).thenReturn(updatedMemberDto);

        String memberJson = objectMapper.writeValueAsString(member);

        mockMvc.perform(MockMvcRequestBuilders.patch("/member/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test for getting all history of secretary")
    public void testGetAllHistorySecretary() throws Exception {
        List<SecretaryHistoryDto> secretaryHistories = Collections.singletonList(new SecretaryHistoryDto());
        when(memberService.getAllHistorySecretary(1L)).thenReturn(secretaryHistories);

        mockMvc.perform(MockMvcRequestBuilders.get("/member/1/secretary-history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test for getting all history of head")
    public void testGetAllHistoryHead() throws Exception {
        List<HeadHistoryDto> headHistories = Collections.singletonList(new HeadHistoryDto());
        when(memberService.getAllHistoryHead(1L)).thenReturn(headHistories);

        mockMvc.perform(MockMvcRequestBuilders.get("/member/1/head-history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test for getting all academic title history")
    public void testGetAllAcademicTitleHistory() throws Exception {
        List<AcademicTitleHistoryDto> academicTitleHistories = Collections.singletonList(new AcademicTitleHistoryDto());
        when(memberService.getAllAcademicTitleHistory(1L)).thenReturn(academicTitleHistories);

        mockMvc.perform(MockMvcRequestBuilders.get("/member/1/academic-title-history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
