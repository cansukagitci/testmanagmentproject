package com.example.testmanagment.controller;


import com.example.testmanagment.dto.ProjecttoUserDTO;
import com.example.testmanagment.dto.TestDto;
import com.example.testmanagment.dto.TesttoProjectDTO;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.Test;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.service.TestService;
import com.example.testmanagment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

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
   //add
    @PostMapping("/add")
    public ResponseEntity<UserResponse> addTest(@RequestBody TestDto testDto, @RequestHeader("Authorization") String authorization) {

        validateTokenProject(authorization);


        UserResponse response = testService.addTest(testDto);
        return ResponseEntity.ok(response);


    }

    //delete
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteProject(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        UserResponse response = testService.deleteTest(id);


        return ResponseEntity.ok(response);
    }

    //update
    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateTest(@PathVariable Long id, @RequestBody TestDto updateTest, @RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        UserResponse response = testService.updateTest(id,updateTest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<Test> getTests(@RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        return testService.getAllTest();
    }

    @GetMapping("{id}")
    public Optional<Test> getTestById(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        return testService.getTestById(id);
    }


    @PostMapping("/assign-test") // Uygun Endpoint
    public ResponseEntity<UserResponse> assignUserToProject(
            @RequestBody TesttoProjectDTO testtoProjectDTO,
            @RequestHeader("Authorization") String authorization) {

        validateTokenProject(authorization); // Token kontrolü

        UserResponse response = testService.assignTTP(testtoProjectDTO); // Servisteki metodu çağır

        return ResponseEntity.ok(response); // Başarı yanıtı döndür
    }

    // Proje ve etiket ilişkisini kaldırma
    @DeleteMapping("/unassign-test")
    public ResponseEntity<UserResponse> removeUsers(@RequestBody TesttoProjectDTO testtoProjectDTO,@RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        UserResponse response = testService.removeTTP(testtoProjectDTO);
        return ResponseEntity.ok(response);
    }

    ///////////////////////
    @GetMapping("/deleted-tests")
    public List<Test> getDeletedTests(@RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        return testService.getDeletedTests();
    }

    @GetMapping("/active-tests")
    public List<Test> getActiveTests(@RequestHeader("Authorization") String authorization) {
        validateTokenProject(authorization);
        return  testService.getActiveTests();
    }

}
