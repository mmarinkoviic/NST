package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.HeadHistoryConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.HeadHistoryRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeadHistoryServiceImplTest
{
    @Mock
    private HeadHistoryRepository headHistoryRepository;
    @Mock
    private HeadHistoryConverter headHistoryConverter;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberConverter memberConverter;
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private HeadHistoryServiceImpl headHistoryService;
    HeadHistory headHistory;

    @BeforeEach
    public void setupTestData(){

        headHistory =HeadHistory.builder()
                .id(1L)
                .member(Member.builder()
                        .firstname("Jelena")
                        .lastname("Repac")
                        .educationTitle(EducationTitle.builder().name("Associate degree").build())
                        .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                        .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                        .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                        .role(Role.builder().name("Default").build())
                        .build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2)).build();
    }
    @Test
    @DisplayName("JUnit test for get all method")
    void testGetAll() {
        HeadHistory headHistory1 = HeadHistory.builder()
                .member(Member.builder()
                        .firstname("Jovan")
                        .lastname("Ciric")
                        .educationTitle(EducationTitle.builder().name("Associate degree").build())
                        .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                        .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                        .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                        .role(Role.builder().name("Secretary").build())
                        .build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2)).build();

        when(headHistoryRepository.findAll()).thenReturn(Arrays.asList(headHistory, headHistory1));

        when(headHistoryConverter.toDto(headHistory)).thenReturn(HeadHistoryDto.builder()
                .head(MemberDto.builder()
                        .firstname("Jelena")
                        .lastname("Repac")
                        .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                        .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                        .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                        .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                        .role(RoleDto.builder().name("Default").build())
                        .build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2)).build());

        List<HeadHistoryDto> headHistoryDtoList = headHistoryService.getAll();

        verify(headHistoryRepository, times(1)).findAll();

        verify(headHistoryConverter, times(1)).toDto(headHistory);

        assertEquals(2, headHistoryDtoList.size());

    }

    @Test
    @DisplayName("JUnit test for find by id method")

    void testFindByIdHeadHistoryExists() {
        when(headHistoryRepository.findById(1l)).thenReturn(Optional.of(headHistory));

        HeadHistoryDto headHistoryDto = HeadHistoryDto.builder()
                .head(MemberDto.builder()
                        .firstname("Jelena")
                        .lastname("Repac")
                        .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                        .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                        .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                        .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                        .role(RoleDto.builder().name("Default").build())
                        .build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2)).build();
        when(headHistoryConverter.toDto(headHistory)).thenReturn(headHistoryDto);


        HeadHistoryDto foundHeadHistoryDto = headHistoryService.findById(1L);
        verify(headHistoryConverter, times(1)).toDto(headHistory);
        verify(headHistoryRepository,times(1)).findById(1L);

        assertNotNull(foundHeadHistoryDto);
        assertEquals(headHistoryDto.getId(), foundHeadHistoryDto.getId());
    }

    @Test
    @DisplayName("JUnit test for find by id method entity does not exists")

    void testFindByIdHeadHistoryDoesNotExist() {
        when(headHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> headHistoryService.findById(1L));

        verify(headHistoryRepository, times(1)).findById(1L);
        verify(headHistoryConverter, never()).toDto(any());
    }

    @Test
    @DisplayName("JUnit test for delete method")
    void testDeleteDepartmentSuccessfully() {
        Long headId = 1L;
        when(headHistoryRepository.findById(headId)).thenReturn(Optional.of(headHistory));

        headHistoryService.delete(headId);

        verify(headHistoryRepository, times(1)).findById(headId);
        verify(headHistoryRepository, times(1)).delete(headHistory);
    }

    @Test
    @DisplayName("JUnit test for delete method when head history does not exist")
    void testDeleteDepartmentNotFound() {
        Long nonExistingId = 100L;
        when(headHistoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> headHistoryService.delete(nonExistingId));

        verify(headHistoryRepository, times(1)).findById(nonExistingId);
        verify(headHistoryRepository, never()).delete(any());
    }



}
