package nst.springboot.nstapplication.service.impl;

import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.constants.ConstantsCustom;
import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.*;
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


}
