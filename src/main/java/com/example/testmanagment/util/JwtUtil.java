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


    //token oluşturma
    public String generateToken(Long userId,String username){
        Map<String,Object> claims=new HashMap<>();
        claims.put("UserId",userId);
        return  createToken(claims,username);
        
    }


    //token doğrulama

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        if (isTokenExpired(token)) {
            throw new IllegalArgumentException("Token zamanı geçti"); // Token süresi dolmuşsa hata fırlat
        }
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    //token yaratma
    private String createToken(Map<String, Object> claims, String subject) {
        return builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) //
                .signWith(secretKey)
                .compact();
    }

    // Token'dan kullanıcı ID'sini çıkarma
    public Long extractUserId(String token) {
        return (Long) extractAllClaims(token).get("userId"); // Kullanıcı ID'sini döndür
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

    // Token süresi kontrolü
    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
