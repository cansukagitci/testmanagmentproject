package com.example.testmanagment.controller;


import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.User;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.service.ProjectService;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.service.UserService;
import com.example.testmanagment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


       @PostMapping("/add")
    public ResponseEntity<UserResponse> addProject(@RequestBody ProjectDto projectDto) {

           if (projectDto.getUserId() == null) {
               List<UserResponse.UserDetail> details = new ArrayList<>();
               details.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User ID must not be null"));
               return ResponseEntity.badRequest().body(new UserResponse(details)); // Hata mesajını içeren yanıt
           }



        UserResponse response=projectService.addProject(projectDto);
        return ResponseEntity.ok(response);




    }


}
