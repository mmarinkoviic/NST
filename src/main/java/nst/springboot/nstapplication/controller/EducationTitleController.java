package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.service.EducationTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/educationTitle")

public class EducationTitleController {
    //Controller for education title
    //ENDPOINTS -> saving, getAll, getById, partialUpdate
    private EducationTitleService educationTitleService;

    public EducationTitleController(EducationTitleService educationTitleService) {
        this.educationTitleService = educationTitleService;
    }

    @PostMapping
    public ResponseEntity<EducationTitleDto> save(@Valid @RequestBody EducationTitleDto educationTitleDto)  {
        EducationTitleDto titleDTO = educationTitleService.save(educationTitleDto);
        return new ResponseEntity<>(titleDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EducationTitleDto>> getAll() {
        List<EducationTitleDto> titles = educationTitleService.getAll();
        return new ResponseEntity<>(titles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public EducationTitleDto findById(@PathVariable("id") Long id)  {
        return educationTitleService.findById(id);
    }



}
