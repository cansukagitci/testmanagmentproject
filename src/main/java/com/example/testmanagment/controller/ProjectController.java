package com.example.testmanagment.controller;


import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.dto.ProjecttoLabelDTO;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.User;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.service.ProjectService;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.service.UserService;
import com.example.testmanagment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    //define jwt
    @Autowired
    private JwtUtil jwtUtil;


    //validate token
    @GetMapping("/validate")
    public ResponseEntity<String> validateTokenProject(@RequestParam String token) {
        try {


            if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                if (jwtUtil.isTokenExpired(token)) {
                    return ResponseEntity.status(401).body("Token is expired");
                }
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(401).body("Token is invalid");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


    @PostMapping("/add")
    public ResponseEntity<UserResponse> addProject(@RequestBody ProjectDto projectDto, @RequestHeader("Authorization") String authorization) {

        validateTokenProject(authorization);


        UserResponse response = projectService.addProject(projectDto);
        return ResponseEntity.ok(response);


    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //delete
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteProject(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        UserResponse response = projectService.deleteProject(id);


        return ResponseEntity.ok(response);
    }

    //update
    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateProject(@PathVariable Long id, @RequestBody Project updateProject,@RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        UserResponse response = projectService.updateProject(id, updateProject);
        return ResponseEntity.ok(response);
    }


    /////////////////////////////////////////////////////////////////////////////
    //assign label


    @PostMapping("/assign-label") // Uygun Endpoint
    public ResponseEntity<UserResponse> assignLabelToProject(
            @RequestBody ProjecttoLabelDTO projectToLabelDto,
            @RequestHeader("Authorization") String authorization) {

        validateTokenProject(authorization); // Token kontrolü

        UserResponse response = projectService.assignPTL(projectToLabelDto); // Servisteki metodu çağır

        return ResponseEntity.ok(response); // Başarı yanıtı döndür
    }


    @GetMapping
    public List<Project> getProjects(@RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        return projectService.getAllProjects();
    }

    @GetMapping("{id}")
    public Optional<Project> getProjectById(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        return projectService.getProjectById(id);
    }

}
