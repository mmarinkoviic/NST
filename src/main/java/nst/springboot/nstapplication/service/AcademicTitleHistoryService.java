package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.AcademicTitleHistoryDto;

import java.util.List;

public interface AcademicTitleHistoryService {

    AcademicTitleHistoryDto save(AcademicTitleHistoryDto academicTitleHistoryDto);
    List<AcademicTitleHistoryDto> getAll();
    AcademicTitleHistoryDto findById(Long id);
    List<AcademicTitleHistoryDto> findByMemberId(Long id);

    AcademicTitleHistoryDto update(Long id, AcademicTitleHistoryDto academicTitleHistoryDto);
}
