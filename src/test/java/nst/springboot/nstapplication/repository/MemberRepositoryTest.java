package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
 class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setupTestData(){
        member = Member.builder()
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
    @DisplayName("JUnit test for save member operation")
     void givenMemberObject_whenSave_thenReturnSaveMember(){

        Member saveMember = memberRepository.save(member);

        assertThat(saveMember).isNotNull();
        assertThat(saveMember.getId()).isPositive();
        assertEquals(member.getFirstname(), saveMember.getFirstname());
    }
    @Test
    @DisplayName("JUnit test for find all members operation")
     void givenMemberList_whenFindAll_thenMemberList(){
        Member member1 = Member.builder()
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();

        Member member2 = Member.builder()
                .firstname("Jovan")
                .lastname("Ciric")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);


        List<Member> members = memberRepository.findAll();

        assertThat(members).isNotNull();
        assertEquals(members.size(), 2);
    }
    @Test
    @DisplayName("JUnit test for find by id member operation")
     void givenMemberId_whenFindById_thenReturnMemberObject(){
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for finding all members by department id")
     void givenDepartmentId_whenFindAllByDepartmentId_thenReturnMemberList(){
        memberRepository.save(member);

        List<Member> result = memberRepository.findAllByDepartmentId(member.getDepartment().getId());
        assertEquals(1, result.size());

    }
    @Test
    @DisplayName("JUnit test for delete member operation")
     void givenMemberObject_whenDelete_thenRemoveMember() {
        memberRepository.save(member);

        memberRepository.deleteById(member.getId());
        Optional<Member> deletedMember = memberRepository.findById(member.getId());

        assertThat(deletedMember).isEmpty();
    }
    @Test
    @DisplayName("JUnit test for finding member by firstname and lastname")
     void givenMemberNameAndLastname_whenFindByNameAndLastname_thenReturnMemberObject(){
        memberRepository.save(member);

        Member memberDb = memberRepository.findByFirstnameAndLastname("Jelena", "Repac").get();

        assertEquals(memberDb.getFirstname(), "Jelena");
        assertEquals(memberDb.getLastname(), "Repac");
    }




}
