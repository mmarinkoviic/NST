package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.AcademicTitleHistoryConverter;
import nst.springboot.nstapplication.converter.impl.HeadHistoryConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.AcademicTitleHistory;
import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.AcademicTitleHistoryDto;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.AcademicTitleHistoryRepository;
import nst.springboot.nstapplication.repository.HeadHistoryRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
import nst.springboot.nstapplication.repository.SecretaryHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

 class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberConverter memberConverter;
    @Mock
    private SecretaryHistoryConverter secretaryHistoryConverter;
    @Mock
    private HeadHistoryConverter headHistoryConverter;
    @Mock
    private SecretaryHistoryRepository secretaryHistoryRepository;
    @Mock
    private HeadHistoryRepository headHistoryRepository;
    @Mock
    private AcademicTitleHistoryConverter academicTitleHistoryConverter;
    @Mock
    private AcademicTitleHistoryRepository academicTitleHistoryRepository;
    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("Test getAll() when members exist")
    void testGetAllMembersExist() {
        List<Member> members = new ArrayList<>();
        members.add(new Member());
        members.add(new Member());

        when(memberRepository.findAll()).thenReturn(members);

        List<MemberDto> memberDtos = memberService.getAll();

        assertNotNull(memberDtos);
        assertEquals(2, memberDtos.size());
    }

    @Test
    @DisplayName("Test getAll() when no members exist")
    void testGetAllNoMembersExist() {
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        EmptyResponseException exception = assertThrows(EmptyResponseException.class, () -> memberService.getAll());

        assertEquals("There are no members in the database!", exception.getMessage());
    }

    @Test
    @DisplayName("Test getAllByDepartmentId() when members for department exist")
    void testGetAllByDepartmentIdMembersExist() {
        Long departmentId = 1L;
        List<Member> members = new ArrayList<>();
        members.add(new Member());
        members.add(new Member());

        when(memberRepository.findAllByDepartmentId(departmentId)).thenReturn(members);

        List<MemberDto> memberDtos = memberService.getAllByDepartmentId(departmentId);

        assertNotNull(memberDtos);
        assertEquals(2, memberDtos.size());
    }

    @Test
    @DisplayName("Test getAllByDepartmentId() when no members for department exist")
    void testGetAllByDepartmentIdNoMembersExist() {
        Long departmentId = 1L;
        when(memberRepository.findAllByDepartmentId(departmentId)).thenReturn(new ArrayList<>());

        EmptyResponseException exception = assertThrows(EmptyResponseException.class, () -> memberService.getAllByDepartmentId(departmentId));

        assertEquals("There are no members for that department!", exception.getMessage());
    }


    @Test
    @DisplayName("Test delete() when member exists")
    void testDeleteMemberExists() {
        Long memberId = 1L;
        Member member = new Member();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        assertDoesNotThrow(() -> memberService.delete(memberId));

        verify(memberRepository, times(1)).deleteById(memberId);
    }

    @Test
    @DisplayName("Test delete() when member does not exist")
    void testDeleteMemberNotExists() {
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> memberService.delete(memberId));

        assertEquals("There is no member with that id in database!", exception.getMessage());

        verify(memberRepository, never()).deleteById(anyLong());
    }


    @Test
    @DisplayName("Test findById() when member exists")
    void testFindByIdMemberExists() {
        Long memberId = 1L;
        MemberDto expectedDto = new MemberDto();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));
        when(memberService.findById(memberId)).thenReturn(expectedDto);

        MemberDto result = memberService.findById(memberId);

        assertNotNull(result);
        assertSame(expectedDto, result);
    }

    @Test
    @DisplayName("Test findById() when member does not exist")
    void testFindByIdMemberNotExists() {
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> memberService.findById(memberId));

        assertEquals("Member does not exist!", exception.getMessage());
    }

    @Test
    @DisplayName("Test getAllHistorySecretary() when member exists")
    void testGetAllHistorySecretaryMemberExists() {
        Long memberId = 1L;
        SecretaryHistoryDto historyDto = new SecretaryHistoryDto();

        Member member = new Member();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        SecretaryHistory secretaryHistory = new SecretaryHistory();
        when(secretaryHistoryRepository.findByMemberId(memberId)).thenReturn(Collections.singletonList(secretaryHistory));

        when(secretaryHistoryConverter.toDto(secretaryHistory)).thenReturn(historyDto);

        List<SecretaryHistoryDto> result = memberService.getAllHistorySecretary(memberId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(historyDto, result.get(0));
    }

    @Test
    @DisplayName("Test getAllHistorySecretary() when member does not exist")
    void testGetAllHistorySecretaryMemberNotExists() {
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> memberService.getAllHistorySecretary(memberId));

        assertEquals("There is no member with that id!", exception.getMessage());
    }

    @Test
    @DisplayName("Test getAllHistoryHead() when member exists")
    void testGetAllHistoryHeadMemberExists() {
        Long memberId = 1L;
        HeadHistoryDto historyDto = new HeadHistoryDto();

        Member member = new Member();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        HeadHistory headHistory = new HeadHistory();
        when(headHistoryRepository.findByMemberId(memberId)).thenReturn(Collections.singletonList(headHistory));

        when(headHistoryConverter.toDto(headHistory)).thenReturn(historyDto);

        List<HeadHistoryDto> result = memberService.getAllHistoryHead(memberId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(historyDto, result.get(0));
    }

    @Test
    @DisplayName("Test getAllHistoryHead() when member does not exist")
    void testGetAllHistoryHeadMemberNotExists() {
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> memberService.getAllHistoryHead(memberId));

        assertEquals("There is no member with that id!", exception.getMessage());
    }


    @Test
    @DisplayName("Test getAllAcademicTitleHistory() when member exists")
    void testGetAllAcademicTitleHistoryMemberExists() {
        Long memberId = 1L;
        AcademicTitleHistoryDto historyDto = new AcademicTitleHistoryDto();

        // Mock setup
        Member member = new Member();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        AcademicTitleHistory academicTitleHistory = new AcademicTitleHistory();
        when(academicTitleHistoryRepository.findByMemberIdOrderByStartDate(memberId))
                .thenReturn(Collections.singletonList(academicTitleHistory));

        // Update the mock setup for AcademicTitleHistoryConverter
        when(academicTitleHistoryConverter.toDto(academicTitleHistory)).thenReturn(historyDto);

        List<AcademicTitleHistoryDto> result = memberService.getAllAcademicTitleHistory(memberId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(historyDto, result.get(0));
    }




}
