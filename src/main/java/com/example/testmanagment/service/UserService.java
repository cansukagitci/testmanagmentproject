package com.example.testmanagment.service;

import com.example.testmanagment.model.User;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
    public UserResponse registerUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();
        try {
            userRepository.save(user);
            userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
        } catch (Exception e) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
        }
        return new UserResponse(userDetails);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // authenticate user
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && BCrypt.checkpw(password, user.getPassword());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //generate token
    public String authenticateUserToken(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getId(), username);
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //delete user
    public UserResponse deleteUser(long userId) {
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();
        try {
            userRepository.deleteById(userId);
            userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
        } catch (Exception e) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
        }
        return new UserResponse(userDetails);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //update user
    public String updateUser(Long userId, User updateUser) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) {
            return "User not found";
        }
        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());
        userRepository.save(existingUser);
        return "User updated";
    }

    ////////////////////////////////////////////////////////
    //get user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //////////////////////////////////////////////////////
    //get user by id
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }
}