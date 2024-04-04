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

 class SecretaryHistoryConverterTest {

        private SecretaryHistoryConverter converter;
        private MemberConverter memberConverter;
        private AcademicTitleConverter academicTitleConverter;
        private ScientificFieldConverter scientificFieldConverter;

        private DepartmentConverter departmentConverter;
        private EducationTitleConverter educationTitleConverter;
        private RoleConverter roleConverter;

        private DepartmentDto departmentDto;
        private Department department;
        private MemberDto memberDto;
        private Member member;
        private SecretaryHistory secretaryHistory;
        private SecretaryHistoryDto secretaryHistoryDto;

        @BeforeEach
        void setUp(){
            departmentConverter = new DepartmentConverter();
            educationTitleConverter = new EducationTitleConverter();
            roleConverter = new RoleConverter();
            academicTitleConverter = new AcademicTitleConverter();
            scientificFieldConverter = new ScientificFieldConverter();
            memberConverter = new MemberConverter(departmentConverter, academicTitleConverter, educationTitleConverter, scientificFieldConverter,roleConverter);
            converter = new SecretaryHistoryConverter(memberConverter,departmentConverter);

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
            secretaryHistoryDto = SecretaryHistoryDto.builder()
                    .member(memberDto)
                    .department(departmentDto)
                    .startDate(LocalDate.now().minusYears(1))
                    .endDate(LocalDate.now().plusYears(2))
                    .build();

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
            secretaryHistory = SecretaryHistory.builder()
                    .member(member)
                    .department(department)
                    .startDate(LocalDate.now().minusYears(1))
                    .endDate(LocalDate.now().plusYears(2))
                    .build();
        }



        @Test
        @DisplayName("Test converting SecretaryHistory to SecretaryHistoryDto")
         void testToDto(){

            SecretaryHistoryDto secretaryHistoryDto= converter.toDto(secretaryHistory);

            assertEquals(secretaryHistoryDto.getId(), secretaryHistory.getId());
            assertEquals(secretaryHistoryDto.getMember().getFirstname(),secretaryHistory.getMember().getFirstname());
            assertEquals(secretaryHistoryDto.getDepartment().getName(), secretaryHistory.getDepartment().getName());
            assertEquals(secretaryHistoryDto.getEndDate(), secretaryHistory.getEndDate());
            assertEquals(secretaryHistoryDto.getStartDate(), secretaryHistory.getStartDate());

        }

        @Test
        @DisplayName("Test converting SecretaryHistoryDto to SecretaryHistory entity")
         void testToEntity(){

            SecretaryHistory secretaryHistory= converter.toEntity(secretaryHistoryDto);

            assertEquals(secretaryHistoryDto.getId(), secretaryHistory.getId());
            assertEquals(secretaryHistoryDto.getMember().getFirstname(),secretaryHistory.getMember().getFirstname());
            assertEquals(secretaryHistoryDto.getDepartment().getName(), secretaryHistory.getDepartment().getName());
            assertEquals(secretaryHistoryDto.getEndDate(), secretaryHistory.getEndDate());
            assertEquals(secretaryHistoryDto.getStartDate(), secretaryHistory.getStartDate());

        }

    @Test
     void testToDtoList() {
        List<SecretaryHistory> secretaryHistories = new ArrayList<>();
        secretaryHistories.add(secretaryHistory);
        secretaryHistories.add(SecretaryHistory.builder().
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


        List<SecretaryHistoryDto> result = converter.toDtoList(secretaryHistories);

        assertEquals(secretaryHistories.size(), result.size());
    }

    @Test
     void testToEntityList() {
        List<SecretaryHistoryDto> secretaryHistories = new ArrayList<>();
        secretaryHistories.add(secretaryHistoryDto);
        secretaryHistories.add(SecretaryHistoryDto.builder().
                member(MemberDto.builder()
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


        List<SecretaryHistory> result = converter.toEntityList(secretaryHistories);

        assertEquals(secretaryHistories.size(), result.size());
    }
}
