package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.dto.DepartmentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartmentConverterTest {


    private final DepartmentConverter converter = new DepartmentConverter();

    @Test
    @DisplayName("Test converting Department entity to DepartmentDto")
    public void testToDto() {
        Department department = Department.builder().id(1L).name("Katedra za informacione tehnologije").shortName("IS").build();
        DepartmentDto departmentDto = converter.toDto(department);

        assertEquals(department.getId(), departmentDto.getId());
        assertEquals(department.getName(), departmentDto.getName());
        assertEquals(department.getShortName(),departmentDto.getShortName());
    }

    @Test
    @DisplayName("Test converting DepartmentDto to Department entity")
    public void testToEntity() {
        DepartmentDto departmentDto = DepartmentDto.builder().id(1L).name("Katedra za informacione tehnologije").shortName("IS").build();
        Department department = converter.toEntity(departmentDto);

        assertEquals(departmentDto.getId(), department.getId());
        assertEquals(departmentDto.getName(), department.getName());
        assertEquals(departmentDto.getShortName(),department.getShortName());
    }
}
