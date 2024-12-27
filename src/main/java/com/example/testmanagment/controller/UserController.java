package com.example.testmanagment.controller;

import com.example.testmanagment.exception.CustomException;
import com.example.testmanagment.model.User;

import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.service.UserService;
import com.example.testmanagment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    ///////////////////////////////////////////////////////////////////////////////////////
    // add user
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody User user) {

      UserResponse response=userService.registerUser(user);
        return ResponseEntity.ok(response);




    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //sign in
    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> signIn(@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticateUser(username, password);
        String token=userService.authenticateUserToken(username,password);
        if (isAuthenticated) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid username or password"));
        }



    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //validate token
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //delete
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String authorization)
    {
        validateToken(authorization);
        UserResponse response=userService.deleteUser(id);


        return ResponseEntity.ok(response);
    }
    /////////////////////////////////////////////////////////////////////////////

    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,@RequestBody User updatedUser,@RequestHeader("Authorization") String authorization){
        validateToken(authorization);
        UserResponse response= userService.updateUser(id,updatedUser);
        return ResponseEntity.ok(response);
    }
    //////////////////////////////////////////////////
    private void validateTokenControl(String authorization) {
        // take token
        String mytoken = authorization.replace("Bearer ", "");

        // use JWT class
        Claims claims = (Claims) validateToken(mytoken);


    }

}