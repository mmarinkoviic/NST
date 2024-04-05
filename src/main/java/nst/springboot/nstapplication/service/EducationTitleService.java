package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.EducationTitleDto;

import java.util.List;
import java.util.Map;

public interface EducationTitleService {

    EducationTitleDto save(EducationTitleDto educationTitleDTO);
    List<EducationTitleDto> getAll();
    EducationTitleDto findById(Long id);

}
