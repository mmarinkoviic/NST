package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeadHistoryConverterTest {


    private HeadHistoryConverter converter;
    private MemberConverter memberConverter;
    private AcademicTitleConverter academicTitleConverter;
    private ScientificFieldConverter scientificFieldConverter;

    private DepartmentConverter departmentConverter;
    private EducationTitleConverter educationTitleConverter;
    private RoleConverter roleConverter;

    @BeforeEach
    void setUp(){
        departmentConverter = new DepartmentConverter();
        educationTitleConverter = new EducationTitleConverter();
        roleConverter = new RoleConverter();
        academicTitleConverter = new AcademicTitleConverter();
        scientificFieldConverter = new ScientificFieldConverter();
        memberConverter = new MemberConverter(departmentConverter, academicTitleConverter, educationTitleConverter, scientificFieldConverter,roleConverter);
        converter = new HeadHistoryConverter(memberConverter,departmentConverter);
    }



    @Test
    @DisplayName("Test converting HeadHistory to HeadHistoryDto entity")
    public void testToDto(){
        Department department = Department.builder().
                id(1L).
                name("Katedra za informacione sisteme i tehnologije").
                shortName("IS").
                build();
        Member member = Member.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();
        HeadHistory headHistory = HeadHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2))
                .build();

        HeadHistoryDto headHistoryDto= converter.toDto(headHistory);

        assertEquals(headHistoryDto.getId(), headHistory.getId());
        assertEquals(headHistoryDto.getHead().getFirstname(),headHistory.getMember().getFirstname());
        assertEquals(headHistoryDto.getDepartment().getName(), headHistory.getDepartment().getName());
        assertEquals(headHistoryDto.getEndDate(), headHistory.getEndDate());
        assertEquals(headHistoryDto.getStartDate(), headHistory.getStartDate());

    }

    @Test
    @DisplayName("Test converting HeadHistoryDto to HeadHistory entity")
    public void testToEntity(){
        DepartmentDto department = DepartmentDto.builder().
                id(1L).
                name("Katedra za informacione sisteme i tehnologije").
                shortName("IS").
                build();
        MemberDto member = MemberDto.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .role(RoleDto.builder().name("Default").build())
                .build();
        HeadHistoryDto headHistoryDto = HeadHistoryDto.builder()
                .head(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2))
                .build();

        HeadHistory headHistory= converter.toEntity(headHistoryDto);

        assertEquals(headHistoryDto.getId(), headHistory.getId());
        assertEquals(headHistoryDto.getHead().getFirstname(),headHistory.getMember().getFirstname());
        assertEquals(headHistoryDto.getDepartment().getName(), headHistory.getDepartment().getName());
        assertEquals(headHistoryDto.getEndDate(), headHistory.getEndDate());
        assertEquals(headHistoryDto.getStartDate(), headHistory.getStartDate());

    }
}
