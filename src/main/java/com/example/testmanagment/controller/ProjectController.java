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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        try{





            if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                if (jwtUtil.isTokenExpired(token)) {
                    return ResponseEntity.status(401).body("Token is expired");
                }
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(401).body("Token is invalid");
            }}catch(IllegalArgumentException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }





    @PostMapping("/add")
    public ResponseEntity<UserResponse> addProject(@RequestBody ProjectDto projectDto,@RequestHeader("Authorization") String authorization) {

           validateTokenProject(authorization);
           if (projectDto.getUserId() == null) {
               List<UserResponse.UserDetail> details = new ArrayList<>();
               details.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User ID must not be null"));
               return ResponseEntity.badRequest().body(new UserResponse(details)); // Hata mesajını içeren yanıt
           }



        UserResponse response=projectService.addProject(projectDto);
        return ResponseEntity.ok(response);




    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //delete
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteProject(@PathVariable Long id, @RequestHeader("Authorization") String authorization)
    {
        validateTokenProject(authorization);
        UserResponse response=projectService.deleteProject(id);


        return ResponseEntity.ok(response);
    }
    /////////////////////////////////////////////////////////////////////////////



}
