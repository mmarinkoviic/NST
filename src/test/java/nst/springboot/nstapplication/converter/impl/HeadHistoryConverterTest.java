package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeadHistoryConverterTest {


    private HeadHistoryConverter converter;
    private MemberConverter memberConverter;
    private AcademicTitleConverter academicTitleConverter;
    private ScientificFieldConverter scientificFieldConverter;

    private DepartmentConverter departmentConverter;
    private EducationTitleConverter educationTitleConverter;
    private RoleConverter roleConverter;
    private Department department;
    private Member member;
    private DepartmentDto departmentDto;
    private MemberDto memberDto;
    private HeadHistory headHistory;
    private HeadHistoryDto headHistoryDto;

    @BeforeEach
    void setUp(){
        departmentConverter = new DepartmentConverter();
        educationTitleConverter = new EducationTitleConverter();
        roleConverter = new RoleConverter();
        academicTitleConverter = new AcademicTitleConverter();
        scientificFieldConverter = new ScientificFieldConverter();
        memberConverter = new MemberConverter(departmentConverter, academicTitleConverter, educationTitleConverter, scientificFieldConverter,roleConverter);
        converter = new HeadHistoryConverter(memberConverter,departmentConverter);

         department = Department.builder().
                id(1L).
                name("Katedra za informacione sisteme i tehnologije").
                shortName("IS").
                build();
         member = Member.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();
         headHistory = HeadHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2))
                .build();

        departmentDto = DepartmentDto.builder().
                id(1L).
                name("Katedra za informacione sisteme i tehnologije").
                shortName("IS").
                build();
        memberDto = MemberDto.builder()
                .id(1L)
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                .role(RoleDto.builder().name("Default").build())
                .build();
        headHistoryDto = HeadHistoryDto.builder()
                .head(memberDto)
                .department(departmentDto)
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2))
                .build();
    }



    @Test
    @DisplayName("Test converting HeadHistory to HeadHistoryDto entity")
    public void testToDto(){
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
        HeadHistory headHistory= converter.toEntity(headHistoryDto);

        assertEquals(headHistoryDto.getId(), headHistory.getId());
        assertEquals(headHistoryDto.getHead().getFirstname(),headHistory.getMember().getFirstname());
        assertEquals(headHistoryDto.getDepartment().getName(), headHistory.getDepartment().getName());
        assertEquals(headHistoryDto.getEndDate(), headHistory.getEndDate());
        assertEquals(headHistoryDto.getStartDate(), headHistory.getStartDate());

    }

    @Test
    public void testToDtoList() {
        List<HeadHistory> headHistoryList = new ArrayList<>();
        headHistoryList.add(headHistory);
        headHistoryList.add(HeadHistory.builder().
                member(Member.builder()
                    .id(1L)
                    .firstname("Jovan")
                    .lastname("Ciric")
                    .educationTitle(EducationTitle.builder().name("Associate degree").build())
                    .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                    .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                    .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                    .role(Role.builder().name("Secretary").build())
                    .build()).
                department( Department.builder().
                    id(1L).
                    name("Katedra za informacione sisteme i tehnologije").
                    shortName("IS").
                    build()).
                endDate(null).
                startDate(LocalDate.now().minusYears(2)).
                build());


        List<HeadHistoryDto> result = converter.toDtoList(headHistoryList);

        assertEquals(headHistoryList.size(), result.size());
    }

    @Test
    public void testToEntityList() {
        List<HeadHistoryDto> headHistoryDtoList = new ArrayList<>();
        headHistoryDtoList.add(headHistoryDto);
        headHistoryDtoList.add(HeadHistoryDto.builder().
                head(MemberDto.builder()
                        .id(1L)
                        .firstname("Jovan")
                        .lastname("Ciric")
                        .educationTitle(EducationTitleDto.builder().name("Associate degree").build())
                        .academicTitle(AcademicTitleDto.builder().name("Teaching Assistant").build())
                        .department(DepartmentDto.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                        .scientificField(ScientificFieldDto.builder().name("Artificial intelligence").build())
                        .role(RoleDto.builder().name("Secretary").build())
                        .build()).
                department( DepartmentDto.builder().
                        id(1L).
                        name("Katedra za informacione sisteme i tehnologije").
                        shortName("IS").
                        build()).
                endDate(LocalDate.now().plusYears(1)).
                startDate(LocalDate.now().minusYears(2)).
                build());


        List<HeadHistory> result = converter.toEntityList(headHistoryDtoList);

        assertEquals(headHistoryDtoList.size(), result.size());
    }
}
