package com.ramadanisafitra.technicaltest.controller;

import com.ramadanisafitra.technicaltest.model.CheckList;
import com.ramadanisafitra.technicaltest.model.CheckListItem;
import com.ramadanisafitra.technicaltest.repository.CheckListItemRepository;
import com.ramadanisafitra.technicaltest.repository.CheckListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
public class ChecklistController {

    private final CheckListRepository checklistRepository;
    private final CheckListItemRepository checkListItemRepository;

    @Autowired
    public ChecklistController(CheckListRepository checklistRepository, CheckListItemRepository checklistItemRepository, CheckListItemRepository checkListItemRepository) {
        this.checklistRepository = checklistRepository;
        this.checkListItemRepository = checkListItemRepository;
    }

    // Get All Checklist
    @GetMapping("/checklist")
    public List<CheckList> getAllChecklists() {
        List<CheckList> checklists = checklistRepository.findAll();
        return checklists;
    }

    // Create new Checklist
    @PostMapping("/checklist")
    public ResponseEntity<String> createChecklist(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        CheckList checklist = new CheckList(name);
        if (name.isEmpty() || name ==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        checklistRepository.save(checklist);
        return new ResponseEntity<>("Checklist created successfully", HttpStatus.OK);
    }

    //Delete Checklist by ID
    @DeleteMapping("/checklist/{checklistId}")
    public ResponseEntity<String> deleteChecklist(@PathVariable("checklistId") Integer checklistId) {
        if (!checklistRepository.existsById(checklistId)) {
            return ResponseEntity.notFound().build();
        }
        checklistRepository.deleteById(checklistId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }

    //Get all Checklist Item by checklist id
    @GetMapping("/checklist/{checklistId}/item")
    public List<CheckListItem> getAllChecklistItems(@PathVariable("checklistId") Integer checklistId) throws ChangeSetPersister.NotFoundException {
        CheckList checklist = checklistRepository.findById(checklistId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        List<CheckListItem> checklistItems = checkListItemRepository.findByChecklist(checklist);

        return checklistItems;
    }

    //Create new checklist item in checklist
    @PostMapping("/checklist/{checklistId}/item")
    public ResponseEntity<String> createChecklistItem(@PathVariable("checklistId") Integer checklistId, @RequestBody Map<String, String> requestBody) throws ChangeSetPersister.NotFoundException {
        CheckList checklist = checklistRepository.findById(checklistId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        String itemName = requestBody.get("itemName");
        if (itemName == null || itemName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CheckListItem checklistItem = CheckListItem.builder()
                .name(itemName)
                .checklist(checklist)
                .status(false)
                .build();

        checkListItemRepository.save(checklistItem);

        return new ResponseEntity<>("Checklist item created successfully", HttpStatus.OK);
    }

    //Get checklist item in checklist by checklist id
    @GetMapping("/checklist/{checklistId}/item/{checklistItemId}")
    public ResponseEntity<CheckListItem> getChecklistItemById(@PathVariable("checklistId") Integer checklistId, @PathVariable("checklistItemId") Long checklistItemId) throws ChangeSetPersister.NotFoundException {
        // Periksa apakah checklist dengan checklistId ada
        CheckList checklist = checklistRepository.findById(checklistId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        // Dapatkan checklist item berdasarkan checklistId dan checklistItemId
        CheckListItem checklistItem = checkListItemRepository.findByChecklistAndId(checklist, checklistItemId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        return new ResponseEntity<>(checklistItem, HttpStatus.OK);
    }

    //Update status checklist item by checklist item id
    @PutMapping("/checklist/{checklistId}/item/{checklistItemId}")
    public ResponseEntity<String> updateChecklistItemStatus(@PathVariable("checklistId") Long checklistId, @PathVariable("checklistItemId") Long checklistItemId) throws ChangeSetPersister.NotFoundException {
        CheckList checklist = checklistRepository.findById(Math.toIntExact(checklistId))
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        Optional<CheckListItem> optionalChecklistItem = checkListItemRepository.findByChecklistAndId(checklist, checklistItemId);
        if (optionalChecklistItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CheckListItem checklistItem = optionalChecklistItem.get();
        checklistItem.setStatus(true);
        checkListItemRepository.save(checklistItem);

        return new ResponseEntity<>("Checklist item status updated successfully", HttpStatus.OK);
    }

    //Delete item by checklist item id
    @DeleteMapping("/checklist/{checklistId}/item/{checklistItemId}")
    public ResponseEntity<String> deleteChecklistItem(@PathVariable("checklistId") Long checklistId, @PathVariable("checklistItemId") Long checklistItemId) throws ChangeSetPersister.NotFoundException {
        CheckList checklist = checklistRepository.findById(Math.toIntExact(checklistId))
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        Optional<CheckListItem> optionalChecklistItem = checkListItemRepository.findByChecklistAndId(checklist, checklistItemId);
        if (optionalChecklistItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CheckListItem checklistItem = optionalChecklistItem.get();
        checkListItemRepository.delete(checklistItem);
        return new ResponseEntity<>("Checklist item deleted successfully", HttpStatus.OK);
    }


    //Rename item by checlist item id
    @PutMapping("/checklist/{checklistId}/item/rename/{checklistItemId}")
    public ResponseEntity<String> renameChecklistItem(@PathVariable("checklistId") Long checklistId,
                                                      @PathVariable("checklistItemId") Long checklistItemId,
                                                      @RequestBody Map<String, String> requestBody)
            throws ChangeSetPersister.NotFoundException {
        CheckList checklist = checklistRepository.findById(Math.toIntExact(checklistId))
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Optional<CheckListItem> optionalChecklistItem = checkListItemRepository.findByChecklistAndId(checklist, checklistItemId);
        if (optionalChecklistItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CheckListItem checklistItem = optionalChecklistItem.get();
        String newItemName = requestBody.get("itemName");
        if (newItemName == null || newItemName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        checklistItem.setName(newItemName);
        checkListItemRepository.save(checklistItem);

        return new ResponseEntity<>("Checklist item renamed successfully", HttpStatus.OK);
    }



}
