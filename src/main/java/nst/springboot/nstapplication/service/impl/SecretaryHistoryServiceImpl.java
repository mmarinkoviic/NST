package nst.springboot.nstapplication.service.impl;

import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.constants.ConstantsCustom;
import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.exception.IllegalArgumentException;
import nst.springboot.nstapplication.repository.*;
import nst.springboot.nstapplication.service.SecretaryHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecretaryHistoryServiceImpl implements SecretaryHistoryService {

    private final SecretaryHistoryRepository repository;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter departmentConverter;
    private final SecretaryHistoryConverter secretaryHistoryConverter;
    private final HeadHistoryRepository headHistoryRepository;
    private final MemberServiceImpl memberService;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public SecretaryHistoryDto save(SecretaryHistoryDto secretaryHistoryDTO) {
        validateDates(secretaryHistoryDTO);

        Optional<Member> existingMember = findOrCreateMember(secretaryHistoryDTO.getMember());
        Optional<Department> existingDepartment = findOrCreateDepartment(secretaryHistoryDTO.getDepartment());

        checkDepartmentMembership(existingDepartment, secretaryHistoryDTO.getMember().getDepartment());

        List<SecretaryHistory> existingHistoryList = repository.findByDepartmentId(secretaryHistoryDTO.getDepartment().getId());
        handleExistingHistories(existingHistoryList, secretaryHistoryDTO);

        handleCurrentHead(existingMember, secretaryHistoryDTO);

        setMemberRole(existingMember, secretaryHistoryDTO);

        return secretaryHistoryConverter.toDto(repository.save(secretaryHistoryConverter.toEntity(secretaryHistoryDTO)));
    }

    private void validateDates(SecretaryHistoryDto secretaryHistoryDTO) {
        if (secretaryHistoryDTO.getStartDate() == null) {
            secretaryHistoryDTO.setStartDate(LocalDate.now());
        }
        if (secretaryHistoryDTO.getEndDate() != null && secretaryHistoryDTO.getEndDate().isBefore(secretaryHistoryDTO.getStartDate())) {
            throw new IllegalArgumentException("End date can't be before start date!");
        }
    }

    private Optional<Member> findOrCreateMember(MemberDto memberDto) {
        if (memberDto.getId() != null) {
            return memberRepository.findById(memberDto.getId());
        } else {
            Optional<Member> existingMember = memberRepository.findByFirstnameAndLastname(memberDto.getFirstname(), memberDto.getLastname());
            if (existingMember.isPresent()) {
                return existingMember;
            } else {
                Member newMember = memberRepository.save(memberConverter.toEntity(memberDto));
                return Optional.of(newMember);
            }
        }
    }

    private Optional<Department> findOrCreateDepartment(DepartmentDto departmentDto) {
        if (departmentDto.getId() != null) {
            return departmentRepository.findById(departmentDto.getId());
        } else {
            Optional<Department> existingDepartment = departmentRepository.findByName(departmentDto.getName());
            if (existingDepartment.isPresent()) {
                return existingDepartment;
            } else {
                Department newDepartment = departmentRepository.save(departmentConverter.toEntity(departmentDto));
                return Optional.of(newDepartment);
            }
        }
    }
    private void handleExistingHistories(List<SecretaryHistory> existingHistoryList, SecretaryHistoryDto secretaryHistoryDTO) {
        for (SecretaryHistory existingHistory : existingHistoryList) {
            Optional<Member> member = memberRepository.findById(existingHistory.getMember().getId());
            if (secretaryHistoryDTO.getEndDate() == null && existingHistory.getEndDate() == null) {
                if (secretaryHistoryDTO.getStartDate().isAfter(existingHistory.getStartDate())) {
                    existingHistory.setEndDate(secretaryHistoryDTO.getStartDate());
                    Optional<Member> activeMember = memberRepository.findById(existingHistory.getMember().getId());
                    if (activeMember.isPresent()) {
                        activeMember.get().setRole(Role.builder().id(ConstantsCustom.DEFAULT_ROLE_ID).name(ConstantsCustom.DEFAULT_ROLE).build());
                        memberService.patchUpdateMember(activeMember.get().getId(), activeMember.get());
                        repository.save(existingHistory);
                    }
                } else {
                    throw new IllegalArgumentException("Member " + existingHistory.getMember().getFirstname() +
                            " " + existingHistory.getMember().getLastname() + " is at the SECRETARY position from " +
                            existingHistory.getStartDate());
                }

            }
            if (existingHistory.getStartDate() != null && existingHistory.getEndDate() != null && secretaryHistoryDTO.getStartDate() != null && secretaryHistoryDTO.getEndDate() != null) {
                if (isDateOverlap(existingHistory.getStartDate(), existingHistory.getEndDate(),
                        secretaryHistoryDTO.getStartDate(), secretaryHistoryDTO.getEndDate())) {
                    throw new IllegalArgumentException("The member " + (member.isPresent() ? member.get().getFirstname() : " ") + " " +
                            (member.isPresent() ? member.get().getLastname() : " ") +
                            " already was at the SECRETARY position from " + existingHistory.getStartDate() + " to " + existingHistory.getEndDate() +
                            " in department " + secretaryHistoryDTO.getDepartment().getName());
                }
            }
        }
    }

    private void handleCurrentHead(Optional<Member> existingMember, SecretaryHistoryDto secretaryHistoryDTO) {
        Optional<HeadHistory> activeHead = headHistoryRepository.findCurrentByMemberId(secretaryHistoryDTO.getMember().getId());
        if (activeHead.isPresent()) {
            if (secretaryHistoryDTO.getEndDate() == null &&
                    (secretaryHistoryDTO.getStartDate().isAfter(activeHead.get().getStartDate()) || secretaryHistoryDTO.getStartDate().isEqual(activeHead.get().getStartDate()))) {
                throw new IllegalArgumentException("Member " + secretaryHistoryDTO.getMember().getFirstname() + " " + secretaryHistoryDTO.getMember().getLastname() + " " +
                        "can't be SECRETARY because member is at the HEAD position from " + activeHead.get().getStartDate() + " for department " + activeHead.get().getDepartment().getName());
            }

        }
    }

    private void setMemberRole(Optional<Member> existingMember, SecretaryHistoryDto secretaryHistoryDTO) {
        if (secretaryHistoryDTO.getStartDate() != null &&
                (secretaryHistoryDTO.getStartDate().isBefore(LocalDate.now()) || secretaryHistoryDTO.getStartDate().isEqual(LocalDate.now())) &&
                (secretaryHistoryDTO.getEndDate() == null || secretaryHistoryDTO.getEndDate().isAfter(LocalDate.now()))) {
            existingMember.get().setRole(Role.builder().name(ConstantsCustom.SECRETARY).id(ConstantsCustom.SECRETARY_ROLE_ID).build());
            secretaryHistoryDTO.setMember(memberService.patchUpdateMember(existingMember.get().getId(), existingMember.get()));
        }
    }

    private void checkDepartmentMembership(Optional<Department> existingDepartment, DepartmentDto memberDepartment) {
        if (!existingDepartment.isPresent() || !existingDepartment.get().getId().equals(memberDepartment.getId())) {
            throw new IllegalArgumentException("Member is not in the specified department!");
        }
    }

    public boolean isDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return startDate1.isBefore(endDate2) && endDate1.isAfter(startDate2);
    }

    @Override
    public List<SecretaryHistoryDto> getAll() {
        List<SecretaryHistoryDto> secretaryHistoryDtoList =  repository
                .findAll()
                .stream()
                .map(secretaryHistoryConverter::toDto)
                .collect(Collectors.toList());

        if (secretaryHistoryDtoList.isEmpty()) {
            throw new EntityNotFoundException("");
        }

        return secretaryHistoryDtoList;
    }
    @Override
    public SecretaryHistoryDto getByDepartmentId(Long id){
        Optional<SecretaryHistory> secretaryHistory = repository.findByDepartmentIdAndEndDateNull(id);
        if(secretaryHistory.isEmpty()){
            throw new EntityNotFoundException("Department doesn't have active secretary member");
        }
        return secretaryHistoryConverter.toDto(secretaryHistory.get());
    }

    @Override
    public void delete(Long id)  {
        Optional<SecretaryHistory> history = repository.findById(id);
        if (history.isPresent()) {
            SecretaryHistory history1 = history.get();
            repository.delete(history1);
        } else {
            throw new EntityNotFoundException("Secretary history does not exist!");
        }
    }

    @Override
    public SecretaryHistoryDto findById(Long id) {
        Optional<SecretaryHistory> history = repository.findById(id);
        if (history.isPresent()) {
            SecretaryHistory history1 = history.get();
            return secretaryHistoryConverter.toDto(history1);
        } else {
            throw new EntityNotFoundException("Secretary history not exist!");
        }
    }

    @Override
    public List<SecretaryHistoryDto> getHistoryForDepartmentId(Long id) {
        List<SecretaryHistory> secretaryHistoryList = repository.findByDepartmentId(id);
        if(secretaryHistoryList.isEmpty()){
            throw new EmptyResponseException("There are no secretary history for department!");
        }
        List<SecretaryHistoryDto> secretaryHistoryDtoList= new ArrayList<>();
        for(SecretaryHistory sc : secretaryHistoryList){
            secretaryHistoryDtoList.add(secretaryHistoryConverter.toDto(sc));
        }
        return secretaryHistoryDtoList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public SecretaryHistoryDto patchSecretaryHistory(Long id, SecretaryHistoryDto secretaryHistoryDto) {

        if(secretaryHistoryDto.getEndDate()!=null && secretaryHistoryDto.getStartDate()!= null){
            if(secretaryHistoryDto.getEndDate().isBefore(secretaryHistoryDto.getStartDate())){
                throw new IllegalArgumentException("End date can't be before start date!");
            }
        }

        Optional<SecretaryHistory> existingSecretaryHistory = repository.findById(id);
        if(!existingSecretaryHistory.isPresent()){
            throw new EntityNotFoundException("There is no history with that id!");
        }

        if(secretaryHistoryDto.getMember() == null){
            secretaryHistoryDto.setMember(memberConverter.toDto(existingSecretaryHistory.get().getMember()));
        }
        Optional<Member> existingMember = memberRepository.findById(secretaryHistoryDto.getMember().getId());
        if(!existingMember.isPresent()){
            throw new IllegalArgumentException("There is no member with that id!");
        }
        secretaryHistoryDto.setMember(memberConverter.toDto(existingMember.get()));
        if(!secretaryHistoryDto.getMember().getId().equals(existingSecretaryHistory.get().getMember().getId())){
            throw new IllegalArgumentException("You want to change history for member "+secretaryHistoryDto.getMember().getFirstname()+" "+secretaryHistoryDto.getMember().getLastname()+
                    ", but the specific history is for "+existingSecretaryHistory.get().getMember().getFirstname()+" "+existingSecretaryHistory.get().getMember().getLastname());

        }

        if(secretaryHistoryDto.getDepartment() == null){
            secretaryHistoryDto.setDepartment(departmentConverter.toDto(existingSecretaryHistory.get().getDepartment()));
        }
        Optional<Department> existingDepartment = departmentRepository.findById(secretaryHistoryDto.getDepartment().getId());
        if(!existingDepartment.isPresent()){
            throw new IllegalArgumentException("There is no department with that id!");
        }
        secretaryHistoryDto.setDepartment(departmentConverter.toDto(existingDepartment.get()));
        if(!secretaryHistoryDto.getDepartment().getId().equals(existingSecretaryHistory.get().getDepartment().getId())){
            throw new IllegalArgumentException("You want to change history for department "+secretaryHistoryDto.getDepartment().getName()+
                    ", but the specific history is for "+existingSecretaryHistory.get().getDepartment().getName());

        }

        //Istorija za konkretnu katedru
        List<SecretaryHistory> existingHistories = repository.findByDepartmentId(secretaryHistoryDto.getDepartment().getId());
        for(SecretaryHistory secretaryHistory : existingHistories) {
            if (secretaryHistoryDto.getStartDate() != null && secretaryHistoryDto.getEndDate() != null && secretaryHistory.getStartDate() != null && secretaryHistory.getEndDate() != null) {
                if (isDateOverlap(secretaryHistoryDto.getStartDate(), secretaryHistoryDto.getEndDate(),
                        secretaryHistory.getStartDate(), secretaryHistory.getEndDate())) {
                    throw new IllegalArgumentException("The member " + secretaryHistory.getMember().getFirstname() + " " +
                            secretaryHistory.getMember().getLastname() +
                            " already was at the SECRETARY position from " + secretaryHistory.getStartDate() + " to " + secretaryHistory.getEndDate() +
                            " in department " + secretaryHistory.getDepartment().getName());
                }
            }
        }
        //aktivan sekretar
        Optional<SecretaryHistory> activeSecretary = repository.findCurrentSecretaryByDepartmentId(secretaryHistoryDto.getDepartment().getId(), LocalDate.now());
        if (activeSecretary.isPresent() &&
                (secretaryHistoryDto.getEndDate() == null &&
                        secretaryHistoryDto.getStartDate().isAfter(activeSecretary.get().getStartDate()))) {
            // Aktivni sekretar se setuje na default role u
            activeSecretary.get().setEndDate(secretaryHistoryDto.getStartDate());
            Optional<Member> member = memberRepository.findById(activeSecretary.get().getMember().getId());
            if (member.isPresent()) {
                Member memberDb = member.get();
                Optional<Role> role = roleRepository.findById(ConstantsCustom.DEFAULT_ROLE_ID);
                Role roleDb = new Role();
                if (role.isPresent()) {
                    roleDb = role.get();
                }
                memberDb.setRole(roleDb);
                activeSecretary.get().setMember(memberConverter.toEntity(memberService.patchUpdateMember(member.get().getId(), existingMember.get())));
                repository.save(activeSecretary.get());
            }
        }


        existingSecretaryHistory.get().setStartDate(secretaryHistoryDto.getStartDate());
        existingSecretaryHistory.get().setEndDate(secretaryHistoryDto.getEndDate());

        if(existingSecretaryHistory.get().getStartDate()!=null &&
                (existingSecretaryHistory.get().getStartDate().isBefore(LocalDate.now()) || existingSecretaryHistory.get().getStartDate().isEqual(LocalDate.now())) &&
                (existingSecretaryHistory.get().getEndDate()==null || existingSecretaryHistory.get().getEndDate().isAfter(LocalDate.now()))){
            existingMember.get().setRole(Role.builder().id(ConstantsCustom.SECRETARY_ROLE_ID).name(ConstantsCustom.SECRETARY).build());
            existingSecretaryHistory.get().setMember(memberConverter.toEntity(memberService.patchUpdateMember(existingMember.get().getId(),existingMember.get())));
        }
        return secretaryHistoryConverter.toDto(repository.save(existingSecretaryHistory.get()));
    }
}
