package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.service.HeadHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/head")
public class HeadHistoryController {
    private HeadHistoryService service;

    public HeadHistoryController(HeadHistoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HeadHistoryDto> save(@Valid @RequestBody HeadHistoryDto headHistoryDto) {
        return new ResponseEntity<>(service.save(headHistoryDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HeadHistoryDto>> getAll() {
        List<HeadHistoryDto> histories = service.getAll();
        return new ResponseEntity<>(histories, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public HeadHistoryDto findById(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>("Head history removed!", HttpStatus.OK);
    }




}