package com.example.testmanagment.util;
import io.jsonwebtoken.Claims;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import static io.jsonwebtoken.Jwts.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
  //  private String SECRET_KEY="secret";
    private SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username){
        Map<String,Object> claims=new HashMap<>();
        return  createToken(claims,username);
        
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 saat geçerlilik süresi
                .signWith(secretKey)
                .compact();
    }
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Token'ı doğrulama
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
