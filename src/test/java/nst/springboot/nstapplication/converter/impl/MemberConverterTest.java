package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class MemberConverterTest {

    private DepartmentConverter departmentConverter;
    private AcademicTitleConverter academicTitleConverter;
    private EducationTitleConverter educationTitleConverter;
    private ScientificFieldConverter scientificFieldConverter;
    private RoleConverter roleConverter;
    private MemberConverter converter;

    private DepartmentDto departmentDto;
    private MemberDto memberDto;


    private Department department;
    private Member member;
    @BeforeEach
    public void setUp() {
        departmentConverter = mock(DepartmentConverter.class);
        academicTitleConverter = mock(AcademicTitleConverter.class);
        educationTitleConverter = mock(EducationTitleConverter.class);
        scientificFieldConverter = mock(ScientificFieldConverter.class);
        roleConverter = mock(RoleConverter.class);

        converter = new MemberConverter(departmentConverter, academicTitleConverter, educationTitleConverter,
                scientificFieldConverter, roleConverter);

        departmentDto = DepartmentDto.builder()
                .id(1L)
                .name("Katedra za informacione sisteme i tehnologije")
                .shortName("IS")
                .build();
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

        department = Department.builder()
                .id(1L)
                .name("Katedra za informacione sisteme i tehnologije")
                .shortName("IS")
                .build();
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
    }

    @Test
    public void testToDtoList() {
        List<Member> memberList = new ArrayList<>();
        memberList.add(member);

        List<MemberDto> result = converter.toDtoList(memberList);

        assertEquals(memberList.size(), result.size());
    }



}