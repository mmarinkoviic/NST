package nst.springboot.nstapplication.service.impl;

import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.converter.impl.ScientificFieldConverter;
import nst.springboot.nstapplication.domain.ScientificField;
import nst.springboot.nstapplication.dto.ScientificFieldDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.ScientificFieldRepository;
import nst.springboot.nstapplication.service.ScientificFieldService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ScientificFieldServiceImpl implements ScientificFieldService {

    private final ScientificFieldConverter scientificFieldConverter;
    private final ScientificFieldRepository scientificFieldRepository;
    

    @Override
    public ScientificFieldDto save(ScientificFieldDto educationTitleDTO){
        Optional<ScientificField> scField = scientificFieldRepository.findByName(educationTitleDTO.getName());
        if (scField.isPresent()) {
            throw new EntityAlreadyExistsException("Scientific fields with that name already exists!");
        } else {
            ScientificField scientificField = scientificFieldConverter.toEntity(educationTitleDTO);
            return scientificFieldConverter.toDto(scientificFieldRepository.save(scientificField));
        }
    }

    @Override
    public List<ScientificFieldDto> getAll() {
        return scientificFieldRepository.findAll()
                .stream()
                .map(scientificFieldConverter::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public ScientificFieldDto findById(Long id) {
        Optional<ScientificField> scientificField = scientificFieldRepository.findById(id);
        if (scientificField.isPresent()) {
            return scientificFieldConverter.toDto(scientificField.get());
        } else {
            throw new EntityNotFoundException("Scientific field does not exist!");
        }
    }



}
