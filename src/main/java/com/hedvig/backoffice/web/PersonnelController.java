package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.personnel.PersonnelServiceException;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/personnel")
public class PersonnelController {

    private PersonnelService personnelService;

    @Autowired
    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @PutMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid PersonnelDTO dto) throws PersonnelServiceException {
        personnelService.register(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid PersonnelDTO dto, @PathVariable long id) throws PersonnelServiceException {
        personnelService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) throws PersonnelServiceException {
        personnelService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public List<PersonnelDTO> list() {
        return personnelService.list();
    }

}
