package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.ScientificFieldConverter;
import nst.springboot.nstapplication.domain.ScientificField;
import nst.springboot.nstapplication.dto.ScientificFieldDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.ScientificFieldRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
@ExtendWith(MockitoExtension.class)

 class ScientificFieldServiceImplTest {

    @Mock
    private ScientificFieldRepository scientificFieldRepository;

    @Mock
    private ScientificFieldConverter scientificFieldConverter;

    @InjectMocks
    private ScientificFieldServiceImpl scientificFieldService;

    @Test
    void testSaveNewScientificFieldSuccessfullySaved() {

        ScientificFieldDto scientificFieldDto = new ScientificFieldDto();
        scientificFieldDto.setName("Computer Science");
        scientificFieldDto.setId(1L);

        when(scientificFieldRepository.findByName("Computer Science")).thenReturn(Optional.empty());
        when(scientificFieldConverter.toEntity(scientificFieldDto)).thenReturn(new ScientificField());
        when(scientificFieldConverter.toEntity(scientificFieldDto)).thenReturn(new ScientificField(scientificFieldDto.getId(), scientificFieldDto.getName()));
        when(scientificFieldConverter.toDto(any())).thenReturn(scientificFieldDto);

        ScientificFieldDto savedScientificFieldDto = scientificFieldService.save(scientificFieldDto);

        verify(scientificFieldRepository, times(1)).save(any());
        assertNotNull(savedScientificFieldDto);
    }

    @Test
    void testSaveExistingScientificFieldThrowsEntityAlreadyExistsException() {
        ScientificFieldDto scientificFieldDto = new ScientificFieldDto();
        scientificFieldDto.setName("Computer Science");

        when(scientificFieldRepository.findByName("Computer Science")).thenReturn(Optional.of(new ScientificField()));

        assertThrows(EntityAlreadyExistsException.class, () -> scientificFieldService.save(scientificFieldDto));
    }


    @Test
    void testGetAll() {
        ScientificField scientificField1 = new ScientificField(1L, "Computer Science");
        ScientificField scientificField2 = new ScientificField(2L, "Artificial intelligence");

        when(scientificFieldRepository.findAll()).thenReturn(Arrays.asList(scientificField1, scientificField2));

        when(scientificFieldConverter.toDto(scientificField1)).thenReturn(new ScientificFieldDto(1L, "Computer Science"));
        when(scientificFieldConverter.toDto(scientificField2)).thenReturn(new ScientificFieldDto(2L, "Artificial intelligence"));

        List<ScientificFieldDto> scientificFieldDtoList = scientificFieldService.getAll();

        verify(scientificFieldRepository, times(1)).findAll();

        verify(scientificFieldConverter, times(1)).toDto(scientificField1);
        verify(scientificFieldConverter, times(1)).toDto(scientificField2);

        assertEquals(2, scientificFieldDtoList.size());
        assertEquals("Computer Science", scientificFieldDtoList.get(0).getName());
        assertEquals("Artificial intelligence", scientificFieldDtoList.get(1).getName());
    }
    @Test
    void testFindByIdScientificFieldExists() {
        ScientificField scientificField = new ScientificField(1L, "Computer Science");
        when(scientificFieldRepository.findById(1l)).thenReturn(Optional.of(scientificField));

        ScientificFieldDto scientificFieldDto = new ScientificFieldDto(1L, "Computer Science");
        when(scientificFieldConverter.toDto(scientificField)).thenReturn(scientificFieldDto);


        ScientificFieldDto foundFieldDto = scientificFieldService.findById(1L);
        System.out.println(foundFieldDto.getName());
        verify(scientificFieldConverter, times(1)).toDto(scientificField);
        verify(scientificFieldRepository,times(1)).findById(1L);

        assertNotNull(foundFieldDto);
        assertEquals(scientificFieldDto.getId(), foundFieldDto.getId());
        assertEquals(scientificFieldDto.getName(), foundFieldDto.getName());
    }
    @Test
    void testFindByIdScientificFieldDoesNotExist() {
        when(scientificFieldRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> scientificFieldService.findById(1L));

        verify(scientificFieldRepository, times(1)).findById(1L);
        verify(scientificFieldConverter, never()).toDto(any());
    }


    @Test
    void testPartialUpdateScientificFieldNotFound() {
        when(scientificFieldRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, String> updates = new HashMap<>();
        updates.put("name", "Computer Science");

        assertThrows(EntityNotFoundException.class, () -> scientificFieldService.partialUpdate(1L, updates));

        verify(scientificFieldRepository, times(1)).findById(1L);

        verify(scientificFieldRepository, never()).save(any());

        verify(scientificFieldConverter, never()).toDto(any());
    }

    @Test
    @DisplayName("JUnit test for partialUpdate method when updating name")
    void testPartialUpdateName() {
        Long scientificFieldId = 1L;
        String updatedName = "Updated Scientific Field Name";
        Map<String, String> updates = new HashMap<>();
        updates.put("name", updatedName);

        ScientificField scientificField = new ScientificField();
        scientificField.setId(scientificFieldId);
        scientificField.setName("Initial Scientific Field Name");

        ScientificField savedScientificField = new ScientificField();
        savedScientificField.setId(scientificFieldId);
        savedScientificField.setName(updatedName);

        ScientificFieldDto expectedDto = new ScientificFieldDto();
        expectedDto.setId(scientificFieldId);
        expectedDto.setName(updatedName);

        when(scientificFieldRepository.findById(scientificFieldId)).thenReturn(Optional.of(scientificField));
        when(scientificFieldRepository.save(any(ScientificField.class))).thenAnswer(invocation -> {
            ScientificField argument = invocation.getArgument(0);
            argument.setName(updatedName);
            return savedScientificField;
        });
        when(scientificFieldConverter.toDto(any(ScientificField.class))).thenReturn(expectedDto);

        ScientificFieldDto result = scientificFieldService.partialUpdate(scientificFieldId, updates);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(scientificFieldRepository, times(1)).findById(scientificFieldId);
        verify(scientificFieldRepository, times(1)).save(any(ScientificField.class));
        verify(scientificFieldConverter, times(1)).toDto(any(ScientificField.class));
    }

}
