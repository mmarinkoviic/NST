package nst.springboot.nstapplication.controller;
import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.AcademicTitleHistoryDto;
import nst.springboot.nstapplication.service.AcademicTitleHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academicTitleHistory")
public class AcademicTitleHistoryController {
    private AcademicTitleHistoryService academicTitleService;

    public AcademicTitleHistoryController(AcademicTitleHistoryService academicTitleService) {
        this.academicTitleService = academicTitleService;
    }

    @PostMapping
    public ResponseEntity<AcademicTitleHistoryDto> save(@Valid @RequestBody AcademicTitleHistoryDto academicTitleDTO)  {
        AcademicTitleHistoryDto titleDTO = academicTitleService.save(academicTitleDTO);
        return new ResponseEntity<>(titleDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AcademicTitleHistoryDto>> getAll() {
        List<AcademicTitleHistoryDto> titles = academicTitleService.getAll();
        return new ResponseEntity<>(titles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public AcademicTitleHistoryDto findById(@PathVariable("id") Long id) {
        return academicTitleService.findById(id);
    }



    @PutMapping("/{id}")
    public ResponseEntity<AcademicTitleHistoryDto> update(@PathVariable(name = "id") Long id, @Valid @RequestBody AcademicTitleHistoryDto academicTitleHistoryDto)  {
        return new ResponseEntity<>(academicTitleService.update(id, academicTitleHistoryDto), HttpStatus.OK);
    }
}
