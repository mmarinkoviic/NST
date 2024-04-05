package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.HeadHistoryConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class DepartmentServiceImplTest {

    @Mock
    private  DepartmentConverter departmentConverter;
    @Mock
    private  DepartmentRepository departmentRepository;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    @Mock
    private MemberConverter memberConverter;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private HeadHistoryConverter headHistoryConverter;
    @Mock
    private HeadHistoryRepository headHistoryRepository;
    @Mock
    private SecretaryHistoryConverter secretaryHistoryConverter;
    @Mock
    private SecretaryHistoryRepository secretaryHistoryRepository;
    @BeforeEach
    public void setup(){
        department = Department.builder()
                .id(1L)
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();
    }
    @Test
    @DisplayName("JUnit test for saveDepartment method")
    void testSaveNewDepartmentSuccessfullySaved() {
        DepartmentDto departmentDto = DepartmentDto.builder()
                .id(1L)
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();

        when(departmentRepository.findByName("Katedra za informacione tehnologije")).thenReturn(Optional.empty());

        Department departmentEntity = new Department();
        when(departmentConverter.toEntity(departmentDto)).thenReturn(departmentEntity);
        when(departmentRepository.save(any())).thenReturn(new Department());
        when(departmentConverter.toDto(any())).thenReturn(new DepartmentDto());
        DepartmentDto savedDepartment = departmentService.save(departmentDto);
        verify(departmentRepository, times(1)).save(departmentEntity);

        assertNotNull(savedDepartment);
    }

    @Test
    @DisplayName("JUnit test for saveDepartment method Entity already exists exception")
    void testSaveNewDepartmentThrowsEntityAlreadyExistsException(){
        DepartmentDto departmentDto = DepartmentDto.builder()
                .id(1L)
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();

        when(departmentRepository.findByName("Katedra za informacione tehnologije")).thenReturn(Optional.of(new Department()));

        assertThrows(EntityAlreadyExistsException.class,()-> departmentService.save(departmentDto));


    }
    @Test
    @DisplayName("JUnit test for delete method")
    void testDeleteDepartmentSuccessfully() {
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        departmentService.delete(departmentId);

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    @DisplayName("JUnit test for delete method when department does not exist")
    void testDeleteDepartmentNotFound() {
        Long nonExistingDepartmentId = 100L;
        when(departmentRepository.findById(nonExistingDepartmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> departmentService.delete(nonExistingDepartmentId));

        verify(departmentRepository, times(1)).findById(nonExistingDepartmentId);
        verify(departmentRepository, never()).delete(any());
    }
    @Test
    @DisplayName("JUnit test for update method")
    void testUpdateDepartmentSuccessfully() {
        Long departmentId = 1L;
        DepartmentDto updatedDepartmentDto = DepartmentDto.builder()
                .id(departmentId)
                .name("Katedra za IS")
                .shortName("IS")
                .build();
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any())).thenReturn(department);

        DepartmentDto result = departmentService.update(departmentId, updatedDepartmentDto);

        assertEquals(updatedDepartmentDto.getName(), result.getName());
        assertEquals(updatedDepartmentDto.getShortName(), result.getShortName());

        verify(departmentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("JUnit test for update method entity not found")
    void testUpdateDepartmentEntityNotFound() {
        Long departmentId = 1L;
        DepartmentDto updatedDepartmentDto = DepartmentDto.builder()
                .id(departmentId)
                .name("Katedra za IS")
                .shortName("IS")
                .build();
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> departmentService.update(departmentId, updatedDepartmentDto));

    }

    @Test
    @DisplayName("JUnit test for findById method")
    void testFindByIdDepartmentSuccessfully() {
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        DepartmentDto expectedDepartmentDto = departmentConverter.toDto(department);

        DepartmentDto result = departmentService.findById(departmentId);

        assertEquals(expectedDepartmentDto, result);

        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    @DisplayName("JUnit test for findById method when department does not exist")
    void testFindByIdDepartmentNotFound() {
        Long nonExistingDepartmentId = 100L;
        when(departmentRepository.findById(nonExistingDepartmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> departmentService.findById(nonExistingDepartmentId));

        verify(departmentRepository, times(1)).findById(nonExistingDepartmentId);
        verify(departmentConverter, never()).toDto(any());
    }

    @Test
    @DisplayName("JUnit test for get all departments method")
    void testGetAll() {
        Department department1 = Department.builder().
                id(2L).
                name("Katedra za elektronsko poslovanje").
                shortName("ELAB").
                build();

        given(departmentRepository.findAll()).willReturn(List.of(department,department1));

        List<DepartmentDto> departmentList  = departmentService.getAll();

        assertThat(departmentList)
                .isNotNull()
                .hasSize(2);


    }
    @Test
    @DisplayName("JUnit test for get all departments method not found exception")
    void testGetAllNegativeScenario() {

        given(departmentRepository.findAll()).willReturn(Collections.emptyList());

        assertThrows(EmptyResponseException.class, () -> departmentService.getAll());

    }

    @Test
    @DisplayName("JUnit test for getAllMembersByDepartmentId method with existing department and members")
    void testGetAllMembersByDepartmentIdWithExistingDepartmentAndMembers() {
        Long departmentId = 1L;
        Member member1 = new Member();
        Member member2 = new Member();
        List<Member> memberList = Arrays.asList(member1, member2);
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(memberRepository.findAllByDepartmentId(departmentId)).thenReturn(memberList);
        when(memberConverter.toDtoList(memberList)).thenReturn(Arrays.asList(new MemberDto(), new MemberDto()));

        List<MemberDto> result = departmentService.getAllMembersByDepartmentId(departmentId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(memberRepository, times(1)).findAllByDepartmentId(departmentId);
        verify(memberConverter, times(1)).toDtoList(memberList);
    }

    @Test
    @DisplayName("JUnit test for getAllMembersByDepartmentId method with non-existing department")
    void testGetAllMembersByDepartmentIdWithNonExistingDepartment() {
        Long nonExistingDepartmentId = 1L;
        when(departmentRepository.findById(nonExistingDepartmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> departmentService.getAllMembersByDepartmentId(nonExistingDepartmentId));

        verify(departmentRepository, times(1)).findById(nonExistingDepartmentId);
        verify(memberRepository, never()).findAllByDepartmentId(anyLong());
        verify(memberConverter, never()).toDtoList(any());
    }

    @Test
    @DisplayName("JUnit test for getAllMembersByDepartmentId method with no members")
    void testGetAllMembersByDepartmentIdWithNoMembers() {
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(memberRepository.findAllByDepartmentId(departmentId)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> departmentService.getAllMembersByDepartmentId(departmentId));

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(memberRepository, times(1)).findAllByDepartmentId(departmentId);
        verify(memberConverter, never()).toDtoList(any());
    }

    @Test
    @DisplayName("JUnit test for getHeadHistoryForDepartment method with existing department and head history")
    void testGetHeadHistoryForDepartmentWithExistingDepartmentAndHeadHistory() {
        Long departmentId = 1L;
        HeadHistory headHistory1 = new HeadHistory();
        HeadHistory headHistory2 = new HeadHistory();
        List<HeadHistory> headHistoryList = Arrays.asList(headHistory1, headHistory2);
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(headHistoryRepository.findByDepartmentIdOrderByDate(departmentId)).thenReturn(headHistoryList);
        when(headHistoryConverter.toDtoList(headHistoryList)).thenReturn(Arrays.asList(new HeadHistoryDto(), new HeadHistoryDto()));

        List<HeadHistoryDto> result = departmentService.getHeadHistoryForDepartment(departmentId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(headHistoryRepository, times(1)).findByDepartmentIdOrderByDate(departmentId);
        verify(headHistoryConverter, times(1)).toDtoList(headHistoryList);
    }
    @Test
    @DisplayName("JUnit test for getSecretaryHistoryForDepartment method with existing department and secretary history")
    void testGetSecretaryHistoryForDepartmentWithExistingDepartmentAndHeadHistory() {
        Long departmentId = 1L;
        SecretaryHistory secretaryHistory = new SecretaryHistory();
        SecretaryHistory secretaryHistory1 = new SecretaryHistory();
        List<SecretaryHistory> secretaryHistories = Arrays.asList(secretaryHistory, secretaryHistory1);
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(secretaryHistoryRepository.findByDepartmentIdOrderByDate(departmentId)).thenReturn(secretaryHistories);
        when(secretaryHistoryConverter.toDtoList(secretaryHistories)).thenReturn(Arrays.asList(new SecretaryHistoryDto(), new SecretaryHistoryDto()));

        List<SecretaryHistoryDto> result = departmentService.getSecretaryHistoryForDepartment(departmentId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(secretaryHistoryRepository, times(1)).findByDepartmentIdOrderByDate(departmentId);
        verify(secretaryHistoryConverter, times(1)).toDtoList(secretaryHistories);
    }

    @Test
    @DisplayName("JUnit test for getHeadHistoryForDepartment method with non-existing department")
    void testGetHeadHistoryForDepartmentWithNonExistingDepartment() {
        Long nonExistingDepartmentId = 1L;
        when(departmentRepository.findById(nonExistingDepartmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> departmentService.getHeadHistoryForDepartment(nonExistingDepartmentId));

        verify(departmentRepository, times(1)).findById(nonExistingDepartmentId);
        verify(headHistoryRepository, never()).findByDepartmentIdOrderByDate(anyLong());
        verify(headHistoryConverter, never()).toDtoList(any());
    }

    @Test
    @DisplayName("JUnit test for getHeadHistoryForDepartment method with no head history")
    void testGetHeadHistoryForDepartmentWithNoHeadHistory() {
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(headHistoryRepository.findByDepartmentIdOrderByDate(departmentId)).thenReturn(Collections.emptyList());

        assertThrows(EmptyResponseException.class, () -> departmentService.getHeadHistoryForDepartment(departmentId));

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(headHistoryRepository, times(1)).findByDepartmentIdOrderByDate(departmentId);
        verify(headHistoryConverter, never()).toDtoList(any());
    }

    @Test
    @DisplayName("JUnit test for getSecretaryHistoryForDepartment method with non-existing department")
    void testGetSecretaryHistoryForDepartmentWithNonExistingDepartment() {
        Long nonExistingDepartmentId = 1L;
        when(departmentRepository.findById(nonExistingDepartmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> departmentService.getSecretaryHistoryForDepartment(nonExistingDepartmentId));

        verify(departmentRepository, times(1)).findById(nonExistingDepartmentId);
        verify(secretaryHistoryRepository, never()).findByDepartmentIdOrderByDate(anyLong());
    }

    @Test
    @DisplayName("JUnit test for getSecretaryHistoryForDepartment method with no secretary history")
    void testGetSecretaryHistoryForDepartmentWithNoHeadHistory() {
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(secretaryHistoryRepository.findByDepartmentIdOrderByDate(departmentId)).thenReturn(Collections.emptyList());

        assertThrows(EmptyResponseException.class, () -> departmentService.getSecretaryHistoryForDepartment(departmentId));

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(secretaryHistoryRepository, times(1)).findByDepartmentIdOrderByDate(departmentId);
    }


    @Test
    void testGetActiveHeadForDepartment_ActiveHeadWithEndDate() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);

        MemberDto expectedMemberDto = new MemberDto();
        HeadHistory activeHead = new HeadHistory();
        activeHead.setMember(new Member());
        activeHead.setStartDate(LocalDate.now().minusDays(1));
        activeHead.setEndDate(LocalDate.now().plusDays(1));

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(headHistoryRepository.findByDepartmentId(departmentId)).thenReturn(Arrays.asList(activeHead));
        when(memberConverter.toDto(activeHead.getMember())).thenReturn(expectedMemberDto);

        MemberDto result = departmentService.getActiveHeadForDepartment(departmentId);

        assertEquals(expectedMemberDto, result);
    }

    @Test
    void testGetActiveHeadForDepartment_ActiveHeadWithoutEndDate() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);

        MemberDto expectedMemberDto = new MemberDto();
        HeadHistory activeHead = new HeadHistory();
        activeHead.setMember(new Member());
        activeHead.setStartDate(LocalDate.now().minusDays(1));
        activeHead.setEndDate(null);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(headHistoryRepository.findByDepartmentId(departmentId)).thenReturn(Arrays.asList(activeHead));
        when(memberConverter.toDto(activeHead.getMember())).thenReturn(expectedMemberDto);

        MemberDto result = departmentService.getActiveHeadForDepartment(departmentId);

        assertEquals(expectedMemberDto, result);
    }

    @Test
    void testGetActiveHeadForDepartment_NoActiveHead() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(headHistoryRepository.findByDepartmentId(departmentId)).thenReturn(Arrays.asList());

        assertThrows(EntityNotFoundException.class, () -> departmentService.getActiveHeadForDepartment(departmentId));
    }

    @Test
    void testGetActiveSecretaryForDepartment_ActiveSecretaryWithEndDate() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);

        MemberDto expectedMemberDto = new MemberDto();
        SecretaryHistory activeSecretary = new SecretaryHistory();
        activeSecretary.setMember(new Member());
        activeSecretary.setStartDate(LocalDate.now().minusDays(1));
        activeSecretary.setEndDate(LocalDate.now().plusDays(1));

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(secretaryHistoryRepository.findByDepartmentId(departmentId)).thenReturn(Arrays.asList(activeSecretary));
        when(memberConverter.toDto(activeSecretary.getMember())).thenReturn(expectedMemberDto);

        MemberDto result = departmentService.getActiveSecretaryForDepartment(departmentId);

        assertEquals(expectedMemberDto, result);
    }

    @Test
    void testGetActiveSecretaryForDepartment_ActiveSecretaryWithoutEndDate() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);

        MemberDto expectedMemberDto = new MemberDto();
        SecretaryHistory activeSecretary = new SecretaryHistory();
        activeSecretary.setMember(new Member());
        activeSecretary.setStartDate(LocalDate.now().minusDays(1));
        activeSecretary.setEndDate(null);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(secretaryHistoryRepository.findByDepartmentId(departmentId)).thenReturn(Arrays.asList(activeSecretary));
        when(memberConverter.toDto(activeSecretary.getMember())).thenReturn(expectedMemberDto);

        MemberDto result = departmentService.getActiveSecretaryForDepartment(departmentId);

        assertEquals(expectedMemberDto, result);
    }

    @Test
    void testGetActiveSecretaryForDepartment_NoActiveSecretary() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(secretaryHistoryRepository.findByDepartmentId(departmentId)).thenReturn(Arrays.asList());

        assertThrows(EntityNotFoundException.class, () -> departmentService.getActiveSecretaryForDepartment(departmentId));
    }
}
