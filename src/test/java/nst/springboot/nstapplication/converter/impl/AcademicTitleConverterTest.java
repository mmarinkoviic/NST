package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcademicTitleConverterTest {

    private final AcademicTitleConverter converter = new AcademicTitleConverter();

    @Test
    @DisplayName("Test converting AcademicTitle entity to AcademicTitleDto")
    public void testToDto() {
        AcademicTitle academicTitle = new AcademicTitle(1L, "Professor");

        AcademicTitleDto academicTitleDto = converter.toDto(academicTitle);

        assertEquals(academicTitle.getId(), academicTitleDto.getId());
        assertEquals(academicTitle.getName(), academicTitleDto.getName());
    }

    @Test
    @DisplayName("Test converting AcademicTitleDto to AcademicTitle entity")
    public void testToEntity() {
        AcademicTitleDto academicTitleDto = new AcademicTitleDto(1L, "Professor");

        AcademicTitle academicTitle = converter.toEntity(academicTitleDto);

        assertEquals(academicTitleDto.getId(), academicTitle.getId());
        assertEquals(academicTitleDto.getName(), academicTitle.getName());
    }
}
