package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.HeadHistoryConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
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
import java.util.ArrayList;
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
    private DepartmentConverter departmentConverter;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private SecretaryHistoryRepository secretaryHistoryRepository;
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
    @Test
    @DisplayName("JUnit test get by department id")
    void testGetByDepartmentIdWithActiveHead() {
        Long departmentId = 1L;
        HeadHistory headHistory = new HeadHistory();
        headHistory.setId(1L);

        when(headHistoryRepository.findByDepartmentIdAndEndDateNull(departmentId)).thenReturn(Optional.of(headHistory));

        HeadHistoryDto headHistoryDto = new HeadHistoryDto();
        headHistoryDto.setId(headHistory.getId());
        when(headHistoryConverter.toDto(headHistory)).thenReturn(headHistoryDto);

        HeadHistoryDto result = headHistoryService.getByDepartmentId(departmentId);

        assertNotNull(result);
        assertEquals(headHistoryDto.getId(), result.getId());
    }
    @Test
    @DisplayName("JUnit test get by department id no active head")
    void testGetByDepartmentIdWithNoActiveHead() {
        Long departmentId = 1L;

        when(headHistoryRepository.findByDepartmentIdAndEndDateNull(departmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> headHistoryService.getByDepartmentId(departmentId));
    }
    @Test
    @DisplayName("JUnit test for get history for department id")
    void testGetHistoryForDepartmentIdWithRecords() {
        Long departmentId = 1L;
        List<HeadHistory> headHistoryList = new ArrayList<>();
        HeadHistory headHistory1 = new HeadHistory();
        headHistory1.setId(1L);
        headHistoryList.add(headHistory1);

        when(headHistoryRepository.findByDepartmentId(departmentId)).thenReturn(headHistoryList);

        List<HeadHistoryDto> headHistoryDtoList = new ArrayList<>();
        HeadHistoryDto headHistoryDto1 = new HeadHistoryDto();
        headHistoryDto1.setId(headHistory1.getId());
        headHistoryDtoList.add(headHistoryDto1);
        when(headHistoryConverter.toDto(headHistory1)).thenReturn(headHistoryDto1);

        List<HeadHistoryDto> result = headHistoryService.getHistoryForDepartmentId(departmentId);

        assertNotNull(result);
        assertEquals(headHistoryList.size(), result.size());
        for (int i = 0; i < headHistoryList.size(); i++) {
            assertEquals(headHistoryList.get(i).getId(), result.get(i).getId());
        }
    }
    @Test
    @DisplayName("JUnit test for get history for department id no records")
    void testGetHistoryForDepartmentIdWithNoRecords() {
        Long departmentId = 1L;

        when(headHistoryRepository.findByDepartmentId(departmentId)).thenReturn(new ArrayList<>());

        assertThrows(EmptyResponseException.class, () -> headHistoryService.getHistoryForDepartmentId(departmentId));
    }

    @Test
    @DisplayName("JUnit test for patching head history")
    void testPatchHeadHistory() {
        Long headHistoryId = 1L;
        HeadHistoryDto headHistoryDto = new HeadHistoryDto();
        headHistoryDto.setStartDate(LocalDate.now().minusDays(1));
        headHistoryDto.setEndDate(null);
        headHistoryDto.setHead(MemberDto.builder().id(1L).build());
        headHistoryDto.setDepartment(DepartmentDto.builder().id(1L).build());

        Member existingMember = new Member();
        existingMember.setId(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(existingMember));

        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment));

        HeadHistory existingHeadHistory = new HeadHistory();
        existingHeadHistory.setId(headHistoryId);
        existingHeadHistory.setStartDate(LocalDate.now().minusDays(2));
        existingHeadHistory.setEndDate(null);
        existingHeadHistory.setMember(existingMember);
        existingHeadHistory.setDepartment(existingDepartment);
        when(headHistoryRepository.findById(headHistoryId)).thenReturn(Optional.of(existingHeadHistory));

        when(headHistoryRepository.save(any(HeadHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(memberConverter.toDto(existingMember)).thenReturn(headHistoryDto.getHead());
        when(departmentConverter.toDto(existingDepartment)).thenReturn(headHistoryDto.getDepartment());

        when(memberService.patchUpdateMember(existingMember.getId(), existingMember)).thenReturn(headHistoryDto.getHead());

        when(headHistoryConverter.toDto(existingHeadHistory)).thenReturn(headHistoryDto);
        HeadHistoryDto result = headHistoryService.patchHeadHistory(headHistoryId, headHistoryDto);

        assertNotNull(result);
        assertEquals(headHistoryDto, result);
    }

    @Test
    @DisplayName("JUnit test for saving head history")
    void testSave() {
        HeadHistoryDto headHistoryDto = new HeadHistoryDto();
        headHistoryDto.setStartDate(null);
        headHistoryDto.setEndDate(null);

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(1L);
        departmentDto.setName("Department A");

        MemberDto memberDto = MemberDto.builder().id(1L).department(departmentDto).build();
        headHistoryDto.setHead(memberDto);

        headHistoryDto.setDepartment(departmentDto);

        Member existingMember = new Member();
        existingMember.setId(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(existingMember));
        when(memberConverter.toDto(existingMember)).thenReturn(headHistoryDto.getHead());

        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment));
        when(departmentConverter.toDto(existingDepartment)).thenReturn(headHistoryDto.getDepartment());

        when(headHistoryRepository.findByDepartmentId(anyLong())).thenReturn(new ArrayList<>());
        when(headHistoryRepository.save(any(HeadHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(memberService.patchUpdateMember(1L, existingMember)).thenReturn(headHistoryDto.getHead());

        when(headHistoryConverter.toDto(any(HeadHistory.class))).thenReturn(headHistoryDto);
        when(headHistoryConverter.toEntity(headHistoryDto)).thenReturn(new HeadHistory());

        HeadHistoryDto result = headHistoryService.save(headHistoryDto);

        assertNotNull(result);
        assertNotNull(result.getStartDate());
        assertEquals(LocalDate.now(), result.getStartDate());
        assertNull(result.getEndDate());
        assertEquals(headHistoryDto.getHead(), result.getHead());
        assertEquals(headHistoryDto.getDepartment(), result.getDepartment());
    }

    @Test
    @DisplayName("JUnit test for saving head history with end date before start date")
    void testSaveWithEndDateBeforeStartDate() {
        HeadHistoryDto headHistoryDto = new HeadHistoryDto();
        headHistoryDto.setStartDate(LocalDate.now());
        headHistoryDto.setEndDate(LocalDate.now().minusDays(1));

        nst.springboot.nstapplication.exception.IllegalArgumentException exception = assertThrows(nst.springboot.nstapplication.exception.IllegalArgumentException.class, () -> headHistoryService.save(headHistoryDto));

        assertEquals("End date can't be before start date!", exception.getMessage());
    }

    @Test
    @DisplayName("JUnit test for saving head history with member not found by ID")
    void testSaveWithMemberNotFoundByID() {
        HeadHistoryDto headHistoryDto = new HeadHistoryDto();
        headHistoryDto.setStartDate(LocalDate.now());
        headHistoryDto.setEndDate(null);
        headHistoryDto.setHead(MemberDto.builder().id(1L).build());

        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> headHistoryService.save(headHistoryDto));

        assertEquals("There is no member with that id!", exception.getMessage());
    }

    @Test
    @DisplayName("JUnit test for saving head history with member not found by firstname and lastname")
    void testSaveWithMemberNotFoundByFirstNameAndLastName() {
        HeadHistoryDto headHistoryDto = new HeadHistoryDto();
        headHistoryDto.setStartDate(LocalDate.now());
        headHistoryDto.setEndDate(null);
        headHistoryDto.setHead(MemberDto.builder().id(1L).build());

        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> headHistoryService.save(headHistoryDto));

        assertEquals("There is no member with that id!", exception.getMessage());
    }

    @Test
    @DisplayName("JUnit test for saving head history with department not found by ID")
    void testSaveWithDepartmentNotFoundByID() {
        HeadHistoryDto headHistoryDto = new HeadHistoryDto();
        headHistoryDto.setStartDate(LocalDate.now());
        headHistoryDto.setEndDate(null);
        headHistoryDto.setDepartment(DepartmentDto.builder().id(1L).build());
        headHistoryDto.setHead(MemberDto.builder().id(1L).build());

        when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member()));
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> headHistoryService.save(headHistoryDto));

        assertEquals("There is no department with that id!", exception.getMessage());
    }

    @Test
    @DisplayName("JUnit test for checking date overlap")
    void testIsDateOverlap() {
        LocalDate startDate1 = LocalDate.of(2022, 1, 1);
        LocalDate endDate1 = LocalDate.of(2022, 1, 10);
        LocalDate startDate2 = LocalDate.of(2022, 1, 5);
        LocalDate endDate2 = LocalDate.of(2022, 1, 15);

        assertTrue(headHistoryService.isDateOverlap(startDate1, endDate1, startDate2, endDate2));

        startDate1 = LocalDate.of(2022, 1, 1);
        endDate1 = LocalDate.of(2022, 1, 10);
        startDate2 = LocalDate.of(2022, 1, 11);
        endDate2 = LocalDate.of(2022, 1, 15);

        assertFalse(headHistoryService.isDateOverlap(startDate1, endDate1, startDate2, endDate2));

        startDate1 = LocalDate.of(2022, 1, 5);
        endDate1 = LocalDate.of(2022, 1, 15);
        startDate2 = LocalDate.of(2022, 1, 1);
        endDate2 = LocalDate.of(2022, 1, 10);

        assertTrue(headHistoryService.isDateOverlap(startDate1, endDate1, startDate2, endDate2));

        startDate1 = LocalDate.of(2022, 1, 11);
        endDate1 = LocalDate.of(2022, 1, 15);
        startDate2 = LocalDate.of(2022, 1, 1);
        endDate2 = LocalDate.of(2022, 1, 10);

        assertFalse(headHistoryService.isDateOverlap(startDate1, endDate1, startDate2, endDate2));
    }



}
