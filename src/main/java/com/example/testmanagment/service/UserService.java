package com.example.testmanagment.service;

import com.example.testmanagment.model.User;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil; // JWT kullanımı için

    // Şifreyi hash'leme
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }


    // Kullanıcı kaydetme
    public User registerUser(User user) {
        user.setPassword(hashPassword(user.getPassword())); // Şifreyi hash'le
        return userRepository.save(user); // Kullanıcıyı veritabanına kaydet
    }



    // Kullanıcı kimlik doğrulaması
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // Hash'lenmiş şifre ile karşılaştır
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false; // Kullanıcı bulunamadı
    }

    public String authenticateUserToken(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // Kullanıcı doğrulama işlemini buraya ekleyin (örneğin, hash karşılaştırması)

            // Token'ı oluştur ve döndür

            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return jwtUtil.generateToken(username); // Doğruysa, token oluştur
            }
        }
        return null; // Giriş başarısız
    }


}
