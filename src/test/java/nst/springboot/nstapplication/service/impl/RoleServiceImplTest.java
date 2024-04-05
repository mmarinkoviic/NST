package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.RoleConverter;
import nst.springboot.nstapplication.domain.Role;
import nst.springboot.nstapplication.dto.RoleDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleConverter roleConverter;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void testSaveNewRoleSuccessfullySaved() {

        RoleDto roleDto = new RoleDto();
        roleDto.setName("Default");
        roleDto.setId(1L);

        when(roleRepository.findByName("Default")).thenReturn(Optional.empty());
        when(roleConverter.toEntity(roleDto)).thenReturn(new Role());
        when(roleConverter.toEntity(roleDto)).thenReturn(new Role(roleDto.getId(), roleDto.getName()));
        when(roleConverter.toDto(any())).thenReturn(roleDto);

        RoleDto savedRoleDto = roleService.save(roleDto);

        verify(roleRepository, times(1)).save(any());
        assertNotNull(savedRoleDto);
    }

    @Test
    void testSaveExistingRoleThrowsEntityAlreadyExistsException() {
        RoleDto roleDto = new RoleDto();
        roleDto.setName("Default");

        when(roleRepository.findByName("Default")).thenReturn(Optional.of(new Role()));

        assertThrows(EntityAlreadyExistsException.class, () -> roleService.save(roleDto));
    }


    @Test
    void testGetAll() {
        Role role1 = new Role(1L, "Default");
        Role role2 = new Role(2L, "Secretary");

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        when(roleConverter.toDto(role1)).thenReturn(new RoleDto(1L, "Default"));
        when(roleConverter.toDto(role2)).thenReturn(new RoleDto(2L, "Secretary"));

        List<RoleDto> roleDtoList = roleService.getAll();

        verify(roleRepository, times(1)).findAll();

        verify(roleConverter, times(1)).toDto(role1);
        verify(roleConverter, times(1)).toDto(role2);

        assertEquals(2, roleDtoList.size());
        assertEquals("Default", roleDtoList.get(0).getName());
        assertEquals("Secretary", roleDtoList.get(1).getName());
    }

    @Test
    void testFindByIdRoleExists() {
        Role role = new Role(1L, "Default");
        when(roleRepository.findById(1l)).thenReturn(Optional.of(role));

        RoleDto roleDto = new RoleDto(1L, "Default");
        when(roleConverter.toDto(role)).thenReturn(roleDto);


        RoleDto foundRoleDto = roleService.findById(1L);
        System.out.println(foundRoleDto.getName());
        verify(roleConverter, times(1)).toDto(role);
        verify(roleRepository,times(1)).findById(1L);

        assertNotNull(foundRoleDto);
        assertEquals(roleDto.getId(), foundRoleDto.getId());
        assertEquals(roleDto.getName(), foundRoleDto.getName());
    }

    @Test
    void testFindByIdRoleDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.findById(1L));

        verify(roleRepository, times(1)).findById(1L);
        verify(roleConverter, never()).toDto(any());
    }


    @Test
    void testPartialUpdateRoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, String> updates = new HashMap<>();
        updates.put("name", "Default");

        assertThrows(EntityNotFoundException.class, () -> roleService.partialUpdate(1L, updates));

        verify(roleRepository, times(1)).findById(1L);

        verify(roleRepository, never()).save(any());

        verify(roleConverter, never()).toDto(any());
    }

    @Test
    @DisplayName("JUnit test for partialUpdate method when successful")
    void testPartialUpdateSuccessful() {
        Long roleId = 1L;
        String updatedName = "Updated Name";
        Map<String, String> updates = new HashMap<>();
        updates.put("name", updatedName);

        Role role = new Role();
        role.setId(roleId);
        role.setName("Initial Name");

        Role savedRole = new Role();
        savedRole.setId(roleId);
        savedRole.setName(updatedName);

        RoleDto roleDto = new RoleDto();
        roleDto.setId(roleId);
        roleDto.setName(updatedName);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
            Role argument = invocation.getArgument(0);
            argument.setName(updatedName);
            return savedRole;
        });
        when(roleConverter.toDto(any(Role.class))).thenReturn(roleDto);

        RoleDto result = roleService.partialUpdate(roleId, updates);

        assertNotNull(result);
        assertEquals(roleDto, result);
        verify(roleRepository, times(1)).findById(roleId);
        verify(roleRepository, times(1)).save(any(Role.class));
        verify(roleConverter, times(1)).toDto(any(Role.class));
    }

}
