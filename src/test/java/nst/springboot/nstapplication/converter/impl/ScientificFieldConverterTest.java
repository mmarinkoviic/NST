package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.ScientificField;
import nst.springboot.nstapplication.dto.ScientificFieldDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScientificFieldConverterTest {

    private final ScientificFieldConverter converter = new ScientificFieldConverter();

    @Test
    @DisplayName("Test converting ScientificField entity to ScientificFieldDto")
    public void testToDto() {
        ScientificField scientificField = new ScientificField(1L, "Scientific computing applications");

        ScientificFieldDto scientificFieldDto = converter.toDto(scientificField);

        assertEquals(scientificField.getId(), scientificFieldDto.getId());
        assertEquals(scientificField.getName(), scientificFieldDto.getName());
    }

    @Test
    @DisplayName("Test converting ScientificFieldDto to ScientificField entity")
    public void testToEntity() {
        ScientificFieldDto scientificFieldDto = new ScientificFieldDto(1L, "Scientific computing applications");

        ScientificField scientificField = converter.toEntity(scientificFieldDto);

        assertEquals(scientificFieldDto.getId(), scientificField.getId());
        assertEquals(scientificFieldDto.getName(), scientificField.getName());
    }
}
