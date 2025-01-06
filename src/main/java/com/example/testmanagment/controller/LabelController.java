package com.example.testmanagment.controller;

import com.example.testmanagment.dto.LabelDto;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        UserResponse response =labelService.deleteLabel(id);
        return ResponseEntity.ok(response);
    }


}
