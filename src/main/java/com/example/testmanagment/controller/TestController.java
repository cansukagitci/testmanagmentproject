package com.example.testmanagment.controller;

import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.dto.TestDto;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/add")
    public ResponseEntity<UserResponse> addTest(@RequestBody TestDto testDto) {




        UserResponse response = testService.addTest(testDto);
        return ResponseEntity.ok(response);


    }


}
