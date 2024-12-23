package com.example.testmanagment.service;

import com.example.testmanagment.model.User;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    //define jwt
    @Autowired
    private JwtUtil jwtUtil;

    //////////////////////////////////////////////////////////////////////////////////////
    // hash password
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // save user
    public User registerUser(User user) {
        logger.debug("started");
        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    // authenticate user
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {

            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //generate token
    public String authenticateUserToken(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null) {


            if (user != null && BCrypt.checkpw(password, user.getPassword())) {

                return jwtUtil.generateToken(user.getId(),username);
            }
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //delete user

    public String deleteUser(long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user.isIsdeleted()) {
            return "user already is deleted";

        }
        if (user != null) {
            user.setIsdeleted(true);
            userRepository.save(user);
            return "User deleted successfully";
        } else {
            return "user not found";
        }





    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //update user
   public String updateUser(Long userId,User updateUser){
        User existingUser=userRepository.findById(userId).orElse(null);
        if(existingUser == null){
            return "User not found";
        }
        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());

        userRepository.save(existingUser);
        return "User updated";
    }

}
