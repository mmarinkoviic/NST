package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.Role;
import nst.springboot.nstapplication.dto.RoleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleConverterTest
{

    private final RoleConverter converter = new RoleConverter();

    @Test
    @DisplayName("Test converting Role entity to RoleDto")
    public void testToDto() {
        Role role = new Role(1L, "Secretary");

        RoleDto roleDto = converter.toDto(role);

        assertEquals(role.getId(), roleDto.getId());
        assertEquals(role.getName(), roleDto.getName());
    }

    @Test
    @DisplayName("Test converting RoleDto to Role entity")
    public void testToEntity() {
        RoleDto roleDto = new RoleDto(1L, "Secretary");

        Role role = converter.toEntity(roleDto);

        assertEquals(roleDto.getId(), role.getId());
        assertEquals(roleDto.getName(), role.getName());
    }
}
