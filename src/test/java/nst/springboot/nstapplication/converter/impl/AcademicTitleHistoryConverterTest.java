package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcademicTitleHistoryConverterTest {
    private AcademicTitleHistoryConverter converter;

    private MemberConverter memberConverter;
    private AcademicTitleConverter academicTitleConverter;
    private ScientificFieldConverter scientificFieldConverter;

    private DepartmentConverter departmentConverter;
    private EducationTitleConverter educationTitleConverter;
    private RoleConverter roleConverter;

    @BeforeEach
    void setUp() {
        departmentConverter = new DepartmentConverter();
        educationTitleConverter = new EducationTitleConverter();
        roleConverter = new RoleConverter();
        academicTitleConverter = new AcademicTitleConverter();
        scientificFieldConverter = new ScientificFieldConverter();
        memberConverter = new MemberConverter(departmentConverter, academicTitleConverter, educationTitleConverter, scientificFieldConverter,roleConverter);
        converter = new AcademicTitleHistoryConverter(memberConverter, academicTitleConverter, scientificFieldConverter);
    }

    @Test
    @DisplayName("Test converting AcademicTitleHistory entity to AcademicTitleHistoryDto")
    public void testToDto() {
        AcademicTitle academicTitle = new AcademicTitle(1L, "Professor");
        Member member = Member.builder()
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();
        ScientificField scientificField = new ScientificField(1L, "Computer science");

        AcademicTitleHistory academicTitleHistory = AcademicTitleHistory.builder().
                id(1L).
                startDate(LocalDate.now().minusYears(2)).
                academicTitle(academicTitle).
                member(member).
                scientificField(scientificField).
                endDate(LocalDate.now().plusYears(2)).
                build();

        AcademicTitleHistoryDto academicTitleHistoryDto = converter.toDto(academicTitleHistory);

        assertEquals(academicTitleHistory.getId(), academicTitleHistoryDto.getId());
        assertEquals(academicTitleHistory.getStartDate(), academicTitleHistoryDto.getStartDate());
        assertEquals(academicTitleHistory.getEndDate(), academicTitleHistoryDto.getEndDate());
        assertEquals(member.getId(), academicTitleHistoryDto.getMember().getId());
        assertEquals(member.getFirstname(), academicTitleHistoryDto.getMember().getFirstname());
        assertEquals(academicTitle.getId(), academicTitleHistoryDto.getAcademicTitle().getId());
        assertEquals(academicTitle.getName(), academicTitleHistoryDto.getAcademicTitle().getName());
        assertEquals(scientificField.getId(), academicTitleHistoryDto.getScientificField().getId());
        assertEquals(scientificField.getName(), academicTitleHistoryDto.getScientificField().getName());
    }

    @Test
    @DisplayName("Test converting AcademicTitleHistoryDto to AcademicTitleHistory entity")
    public void testToEntity() {
        AcademicTitleDto academicTitleDto = new AcademicTitleDto(1L, "Professor");
        MemberDto memberDto = MemberDto.builder()
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .role(RoleDto.builder().name("Default").build())
                .build();
        ScientificFieldDto scientificFieldDto = new ScientificFieldDto(1L, "Field");

        AcademicTitleHistoryDto academicTitleHistoryDto = AcademicTitleHistoryDto.builder().
                id(1L).
                startDate(LocalDate.now().minusYears(2)).
                academicTitle(academicTitleDto).
                member(memberDto).
                scientificField(scientificFieldDto).
                endDate(LocalDate.now().plusYears(2)).
                build();

        AcademicTitleHistory academicTitleHistory = converter.toEntity(academicTitleHistoryDto);

        assertEquals(academicTitleHistoryDto.getId(), academicTitleHistory.getId());
        assertEquals(academicTitleHistoryDto.getStartDate(), academicTitleHistory.getStartDate());
        assertEquals(academicTitleHistoryDto.getEndDate(), academicTitleHistory.getEndDate());
        assertEquals(memberDto.getId(), academicTitleHistory.getMember().getId());
        assertEquals(memberDto.getFirstname(), academicTitleHistory.getMember().getFirstname());
        assertEquals(academicTitleDto.getId(), academicTitleHistory.getAcademicTitle().getId());
        assertEquals(academicTitleDto.getName(), academicTitleHistory.getAcademicTitle().getName());
        assertEquals(scientificFieldDto.getId(), academicTitleHistory.getScientificField().getId());
        assertEquals(scientificFieldDto.getName(), academicTitleHistory.getScientificField().getName());
    }

}
