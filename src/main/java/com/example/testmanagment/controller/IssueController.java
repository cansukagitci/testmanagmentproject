package com.example.testmanagment.controller;

import com.example.testmanagment.dto.IssuesDTO;

import com.example.testmanagment.dto.IssuetoLabelDTO;
import com.example.testmanagment.dto.TesttoProjectDTO;
import com.example.testmanagment.model.Issues;

import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.service.IssuesService;
import com.example.testmanagment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/issue")
public class IssueController {

    @Autowired
    private IssuesService issuesService;
    @Autowired
    private JwtUtil jwtUtil;

    //validate token
    @GetMapping("/validate")
    public ResponseEntity<String> validateTokenIssues(@RequestParam String token) {
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
    @PostMapping("/addissue")
    public ResponseEntity<UserResponse> addLabel(@RequestBody IssuesDTO issuesDTO,@RequestHeader("Authorization") String authorization) {

        validateTokenIssues(authorization);
        UserResponse response = issuesService.addIssue(issuesDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<Issues> getIssues(@RequestHeader("Authorization") String authorization) {
        validateTokenIssues(authorization);
        return issuesService.getAllIssues();
    }

    @GetMapping("{id}")
    public Optional<Issues> getIssueById(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        validateTokenIssues(authorization);
        return issuesService.getIssueById(id);
    }
    //delete
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteProject(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        validateTokenIssues(authorization);
        UserResponse response = issuesService.deleteIssue(id);



        return ResponseEntity.ok(response);
    }

    //update
    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateProject(@PathVariable Long id, @RequestBody IssuesDTO updateIssue, @RequestHeader("Authorization") String authorization) {
        validateTokenIssues(authorization);
        UserResponse response = issuesService.updateIssue(id,updateIssue);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assign-label-issue") // Uygun Endpoint
    public ResponseEntity<UserResponse> assignIssueToLabel(
            @RequestBody IssuetoLabelDTO issuetoLabelDTO,
            @RequestHeader("Authorization") String authorization) {

        validateTokenIssues(authorization); // Token kontrolü

        UserResponse response = issuesService.assignITL(issuetoLabelDTO);


        return ResponseEntity.ok(response); // Başarı yanıtı döndür
    }

    @DeleteMapping("/unassign-label-issue")
    public ResponseEntity<UserResponse> removeUsers(@RequestBody IssuetoLabelDTO issuetoLabelDTO,@RequestHeader("Authorization") String authorization) {
        validateTokenIssues(authorization);
        UserResponse response = issuesService.removeITL(issuetoLabelDTO);
        return ResponseEntity.ok(response);
    }
}
