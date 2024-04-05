package nst.springboot.nstapplication.controller;

import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.service.SecretaryHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secretary")
@Transactional
public class SecretaryHistoryController {
    private SecretaryHistoryService service;

    public SecretaryHistoryController(SecretaryHistoryService service) {
        this.service = service;
    }



    @GetMapping
    public ResponseEntity<List<SecretaryHistoryDto>> getAll() {
        List<SecretaryHistoryDto> histories = service.getAll();
        return new ResponseEntity<>(histories, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public SecretaryHistoryDto findById(@PathVariable("id") Long id){
        return service.findById(id);
    }


//    @GetMapping("/department/{id}")
//    public SecretaryHistoryDto findByDepartmentId(@PathVariable("id") Long id)  {
//        return service.getByDepartmentId(id);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>("Secretary history removed!", HttpStatus.OK);

    }



}