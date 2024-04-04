package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.EducationTitle;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class EducationTitleConverterTest {


    private final EducationTitleConverter converter = new EducationTitleConverter();

    @Test
    @DisplayName("Test converting EducationTitle entity to EducationTitleDto")
     void testToDto() {
        EducationTitle educationTitle = new EducationTitle(1L, "Associate degree");

        EducationTitleDto educationTitleDto = converter.toDto(educationTitle);

        assertEquals(educationTitle.getId(), educationTitleDto.getId());
        assertEquals(educationTitle.getName(), educationTitleDto.getName());
    }

    @Test
    @DisplayName("Test converting EducationTitleDto to EducationTitle entity")
     void testToEntity() {
        EducationTitleDto educationTitleDto = new EducationTitleDto(1L, "Associate degree");

        EducationTitle educationTitle = converter.toEntity(educationTitleDto);

        assertEquals(educationTitleDto.getId(), educationTitle.getId());
        assertEquals(educationTitleDto.getName(), educationTitle.getName());
    }
}
