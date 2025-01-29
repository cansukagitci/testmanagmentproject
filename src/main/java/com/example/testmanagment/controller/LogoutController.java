package com.example.testmanagment.controller;

import com.example.testmanagment.util.TokenBlackList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3001"," http://10.0.0.50:3001"})
public class LogoutController {
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // Bearer token'ı al
        String jwtToken = token.substring(7); // "Bearer " kısmını çıkart

        // Blacklist'e ekle
        TokenBlackList.blacklistToken(jwtToken);


        return ResponseEntity.ok("Logout successful");
    }
}
