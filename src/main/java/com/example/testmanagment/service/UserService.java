package com.example.testmanagment.service;

import ch.qos.logback.core.encoder.EchoEncoder;
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

        // Duplication control for user
        if (userRepository.findByUsername(user.getUsername()) != null) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User is already exist!"));
            return new UserResponse(userDetails);
        }

        // Duplication control for email
        if (userRepository.findByEmail(user.getEmail()) != null) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: E-Mail is already exist!"));
            return new UserResponse(userDetails);
        }
      //empty user control
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Username cannot be empty"));
            return new UserResponse(userDetails);
        }

        //empty email control
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Email cannot be empty"));
            return new UserResponse(userDetails);
        }


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
        Optional<User> deleteUser = userRepository.findById(userId);
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();
        try {
            User user=deleteUser.get();
            if(user != null)
            {
                if(user.isIsdeleted()){
                    userDetails.add(new UserResponse.UserDetail(0, true, "user already is deleted"));
                }else {
                    user.setIsdeleted(true);
                    userRepository.save(user);
                    userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));

                }
            }else {
                userDetails.add(new UserResponse.UserDetail(0, true, "User boş olamaz"));

            }





        } catch (Exception e) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
        }
        return new UserResponse(userDetails);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //update user
    public UserResponse updateUser(Long userId, User updateUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();
        updateUser.setPassword(hashPassword(updateUser.getPassword()));
        try{
            User user=existingUser.get();
            if(existingUser.isEmpty()) {
                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_FAILURE: User must not be null"));
            }else {


                user.setUsername(updateUser.getUsername());
                user.setPassword(updateUser.getPassword());
                user.setEmail(updateUser.getEmail());
                userRepository.save(user);
                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));


            }



        }catch (Exception e){

            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
        }
        return new UserResponse(userDetails);


        /*if(existingUser.isEmpty()) {
            return "User not found";
        }else {

           User user=existingUser.get();
            user.setUsername(updateUser.getUsername());
            user.setEmail(updateUser.getEmail());
            userRepository.save(user);
            return "User updated";
        }*/
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