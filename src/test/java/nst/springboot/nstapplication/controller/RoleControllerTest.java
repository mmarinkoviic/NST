package nst.springboot.nstapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.nstapplication.controller.RoleController;
import nst.springboot.nstapplication.dto.RoleDto;
import nst.springboot.nstapplication.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;


    @Test
    @DisplayName("Test for saving a role")
    public void testSaveRole() throws Exception {
        RoleDto roleDto = new RoleDto();
        roleDto.setName("Default");

        when(roleService.save(any(RoleDto.class))).thenReturn(roleDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(roleDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Test for fetching all roles")
    public void testGetAllRoles() throws Exception {
        RoleDto roleDto = RoleDto.builder().name("Default").build();
        RoleDto roleDto1 = RoleDto.builder().name("Secretary").build();

        when(roleService.getAll()).thenReturn(List.of(roleDto, roleDto1));

        mockMvc.perform(MockMvcRequestBuilders.get("/role")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Default"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Secretary"));
    }

    @Test
    @DisplayName("Test for finding a role by id")
    public void testFindByIdRole() throws Exception {
        RoleDto roleDto = RoleDto.builder().id(1L).name("Secretary").build();

        when(roleService.findById(1L)).thenReturn(roleDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/role/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Secretary"));
    }

    @Test
    @DisplayName("Test for partial updating a role")
    public void testPartialUpdate() throws Exception {
        RoleDto updatedRoleDto = RoleDto.builder().id(1L).name("Secretary").build();

        when(roleService.partialUpdate(1L, getUpdates())).thenReturn(updatedRoleDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/role/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Secretary\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Secretary"));
    }

    private Map<String, String> getUpdates() {
        Map<String, String> updates = new HashMap<>();
        updates.put("name", "Secretary");
        return updates;
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
