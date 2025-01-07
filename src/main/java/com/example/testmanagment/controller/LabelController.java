package com.example.testmanagment.controller;

import com.example.testmanagment.dto.LabelDto;
import com.example.testmanagment.model.Label;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @PostMapping("/addlabel")
    public ResponseEntity<UserResponse> addLabel(@RequestBody LabelDto labelDto) {

        UserResponse response = labelService.addLabel(labelDto);
        return ResponseEntity.ok(response);
    }

    //delete
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteProject(@PathVariable Long id) {

        UserResponse response = labelService.deleteLabel(id);
        return ResponseEntity.ok(response);
    }



    //update
    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateLabel(@PathVariable Long id,@RequestBody Label updateLabel){
        UserResponse response=labelService.updateLabel(id,updateLabel);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<Label> getProjects() {
        return labelService.getAllLabels();
    }

    @GetMapping("{id}")
    public Optional<Label> getLabelById(@PathVariable Long id){
        return labelService.getLabelById(id);
    }


}
