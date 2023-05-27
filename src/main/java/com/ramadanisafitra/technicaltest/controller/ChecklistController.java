package com.ramadanisafitra.technicaltest.controller;

import com.ramadanisafitra.technicaltest.user.Checklist;
import com.ramadanisafitra.technicaltest.user.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class ChecklistController {

    private final ChecklistRepository checklistRepository;

    @Autowired
    public ChecklistController(ChecklistRepository checklistRepository) {
        this.checklistRepository = checklistRepository;
    }

    @GetMapping("/get")
    public List<Checklist> getAllChecklists() {
        List<Checklist> checklists = checklistRepository.findAll();
        return checklists;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createChecklist(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        Checklist checklist = new Checklist(name);
        checklistRepository.save(checklist);
        return new ResponseEntity<>("Checklist created successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{checklistId}")
    public ResponseEntity<String> deleteChecklist(@PathVariable("checklistId") Integer checklistId) {
        if (!checklistRepository.existsById(checklistId)) {
            return ResponseEntity.notFound().build();
        }
        checklistRepository.deleteById(checklistId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}
