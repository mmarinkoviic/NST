package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.HeadHistoryRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
import nst.springboot.nstapplication.repository.SecretaryHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)

 class SecretaryHistoryServiceImplTest {
    @Mock
    private SecretaryHistoryRepository secretaryHistoryRepository;
    @Mock
    private SecretaryHistoryConverter secretaryHistoryConverter;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberConverter memberConverter;
    @Mock
    private DepartmentConverter departmentConverter;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private HeadHistoryRepository headHistoryRepository;
    @InjectMocks
    private SecretaryHistoryServiceImpl secretaryHistoryService;
    SecretaryHistory secretaryHistory;

    @BeforeEach
    public void setupTestData(){

        secretaryHistory =SecretaryHistory.builder()
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
        SecretaryHistory secretaryHistory1 = SecretaryHistory.builder()
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

        when(secretaryHistoryRepository.findAll()).thenReturn(Arrays.asList(secretaryHistory, secretaryHistory1));

        when(secretaryHistoryConverter.toDto(secretaryHistory)).thenReturn(SecretaryHistoryDto.builder()
                .member(MemberDto.builder()
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

        List<SecretaryHistoryDto> secretaryHistoryDtoList = secretaryHistoryService.getAll();

        verify(secretaryHistoryRepository, times(1)).findAll();

        verify(secretaryHistoryConverter, times(1)).toDto(secretaryHistory);

        assertEquals(2, secretaryHistoryDtoList.size());

    }
    @Test
    @DisplayName("JUnit test for find by id method")
    void testFindByIdSecretaryHistoryExists() {
        when(secretaryHistoryRepository.findById(1l)).thenReturn(Optional.of(secretaryHistory));

        SecretaryHistoryDto secretaryHistoryDto = SecretaryHistoryDto.builder()
                .member(MemberDto.builder()
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
        when(secretaryHistoryConverter.toDto(secretaryHistory)).thenReturn(secretaryHistoryDto);


        SecretaryHistoryDto foundSecretaryHistoryDto = secretaryHistoryService.findById(1L);
        verify(secretaryHistoryConverter, times(1)).toDto(secretaryHistory);
        verify(secretaryHistoryRepository,times(1)).findById(1L);

        assertNotNull(foundSecretaryHistoryDto);
        assertEquals(secretaryHistoryDto.getId(), foundSecretaryHistoryDto.getId());
    }
    @Test
    @DisplayName("JUnit test for find by id method entity does not exists")
    void testFindByIdSecretaryHistoryDoesNotExist() {
        when(secretaryHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> secretaryHistoryService.findById(1L));

        verify(secretaryHistoryRepository, times(1)).findById(1L);
        verify(secretaryHistoryConverter, never()).toDto(any());
    }
    @Test
    @DisplayName("JUnit test for delete method")
    void testDeleteDepartmentSuccessfully() {
        Long headId = 1L;
        when(secretaryHistoryRepository.findById(headId)).thenReturn(Optional.of(secretaryHistory));

        secretaryHistoryService.delete(headId);

        verify(secretaryHistoryRepository, times(1)).findById(headId);
        verify(secretaryHistoryRepository, times(1)).delete(secretaryHistory);
    }
    @Test
    @DisplayName("JUnit test for delete method when secretary history does not exist")
    void testDeleteDepartmentNotFound() {
        Long nonExistingId = 100L;
        when(secretaryHistoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> secretaryHistoryService.delete(nonExistingId));

        verify(secretaryHistoryRepository, times(1)).findById(nonExistingId);
        verify(secretaryHistoryRepository, never()).delete(any());
    }
    @Test
    @DisplayName("JUnit test get by department id")
    void testGetByDepartmentIdWithActiveSecretary() {
        Long departmentId = 1L;
        SecretaryHistory secretaryHistory1 = new SecretaryHistory();
        secretaryHistory1.setId(1L);

        when(secretaryHistoryRepository.findByDepartmentIdAndEndDateNull(departmentId)).thenReturn(Optional.of(secretaryHistory));

        SecretaryHistoryDto secretaryHistoryDto = new SecretaryHistoryDto();
        secretaryHistoryDto.setId(secretaryHistory.getId());
        when(secretaryHistoryConverter.toDto(secretaryHistory)).thenReturn(secretaryHistoryDto);

        SecretaryHistoryDto result = secretaryHistoryService.getByDepartmentId(departmentId);

        assertNotNull(result);
        assertEquals(secretaryHistoryDto.getId(), result.getId());
    }
    @Test
    @DisplayName("JUnit test get by department id no active head")
    void testGetByDepartmentIdWithNoActiveSecretary() {
        Long departmentId = 1L;

        when(secretaryHistoryRepository.findByDepartmentIdAndEndDateNull(departmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> secretaryHistoryService.getByDepartmentId(departmentId));
    }
    @Test
    @DisplayName("JUnit test for get history for department id")
    void testGetHistoryForDepartmentIdWithRecords() {
        Long departmentId = 1L;
        List<SecretaryHistory> secretaryHistories = new ArrayList<>();
        SecretaryHistory secretaryHistory1 = new SecretaryHistory();
        secretaryHistory1.setId(1L);
        secretaryHistories.add(secretaryHistory1);

        when(secretaryHistoryRepository.findByDepartmentId(departmentId)).thenReturn(secretaryHistories);

        List<SecretaryHistoryDto> secretaryHistoryDtoList = new ArrayList<>();
        SecretaryHistoryDto secretaryHistoryDto = new SecretaryHistoryDto();
        secretaryHistoryDto.setId(secretaryHistory1.getId());
        secretaryHistories.add(secretaryHistory1);
        when(secretaryHistoryConverter.toDto(secretaryHistory1)).thenReturn(secretaryHistoryDto);

        List<SecretaryHistoryDto> result = secretaryHistoryService.getHistoryForDepartmentId(departmentId);

        assertNotNull(result);
        assertEquals(secretaryHistories.size(), result.size());
        for (int i = 0; i < secretaryHistories.size(); i++) {
            assertEquals(secretaryHistories.get(i).getId(), result.get(i).getId());
        }
    }
    @Test
    @DisplayName("JUnit test for get history for department id no records")
    void testGetHistoryForDepartmentIdWithNoRecords() {
        Long departmentId = 1L;

        when(secretaryHistoryRepository.findByDepartmentId(departmentId)).thenReturn(new ArrayList<>());

        assertThrows(EmptyResponseException.class, () -> secretaryHistoryService.getHistoryForDepartmentId(departmentId));
    }


    @Test
    @DisplayName("Test getAll() when no secretary histories are found")
    void testGetAllWhenNoHistoriesFound() {
        when(secretaryHistoryRepository.findAll()).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> secretaryHistoryService.getAll());

        assertEquals("", exception.getMessage());
    }


    @Test
    @DisplayName("JUnit test for checking date overlap")
    void testIsDateOverlap() {
        LocalDate startDate1 = LocalDate.of(2022, 1, 1);
        LocalDate endDate1 = LocalDate.of(2022, 1, 10);
        LocalDate startDate2 = LocalDate.of(2022, 1, 5);
        LocalDate endDate2 = LocalDate.of(2022, 1, 15);

        assertTrue(secretaryHistoryService.isDateOverlap(startDate1, endDate1, startDate2, endDate2));

        startDate1 = LocalDate.of(2022, 1, 1);
        endDate1 = LocalDate.of(2022, 1, 10);
        startDate2 = LocalDate.of(2022, 1, 11);
        endDate2 = LocalDate.of(2022, 1, 15);

        assertFalse(secretaryHistoryService.isDateOverlap(startDate1, endDate1, startDate2, endDate2));

        startDate1 = LocalDate.of(2022, 1, 5);
        endDate1 = LocalDate.of(2022, 1, 15);
        startDate2 = LocalDate.of(2022, 1, 1);
        endDate2 = LocalDate.of(2022, 1, 10);

        assertTrue(secretaryHistoryService.isDateOverlap(startDate1, endDate1, startDate2, endDate2));

        startDate1 = LocalDate.of(2022, 1, 11);
        endDate1 = LocalDate.of(2022, 1, 15);
        startDate2 = LocalDate.of(2022, 1, 1);
        endDate2 = LocalDate.of(2022, 1, 10);

        assertFalse(secretaryHistoryService.isDateOverlap(startDate1, endDate1, startDate2, endDate2));
    }







}
