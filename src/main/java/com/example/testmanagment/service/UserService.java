package com.example.testmanagment.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.testmanagment.helper.GenericServiceHelper;
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

    @Autowired
    private LogService logService;

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
            logService.logError("Duplicate user : " + user.getUsername());
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User already exists"));

            return new UserResponse(userDetails);
        }

        // Duplication control for email
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logService.logError("Duplicate email : " + user.getEmail());
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: E-Mail is already exist!"));

            return new UserResponse(userDetails);

        }
      //empty user control
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            logService.logError("Empty user");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Username cannot be empty"));
            return new UserResponse(userDetails);
        }

        //empty email control
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            logService.logError("Empty email");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Email cannot be empty"));
            return new UserResponse(userDetails);
        }


        try {


          //  userRepository.save(user);
            logService.logInfo("User registered successfully: " + user.getUsername());
            //userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
            UserResponse response = GenericServiceHelper.saveEntity(user, userRepository,
                    "SERVICE_RESPONSE_SUCCESS", userDetails);


        } catch (Exception e) {
            logService.logError("Service Error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
        }
        return new UserResponse(userDetails);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // authenticate user
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        logService.logInfo("User authenticate successfully: " + user.getUsername());
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
                    logService.logError("User isalready deleted " + user.getUsername());
                    userDetails.add(new UserResponse.UserDetail(0, true, "user already is deleted"));
                }else {
                    user.setIsdeleted(true);
                    logService.logInfo("User deleted successfully: " + user.getUsername());
                   // userRepository.save(user);


                   // userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
                    UserResponse response = GenericServiceHelper.saveEntity(user, userRepository,
                            "SERVICE_RESPONSE_SUCCESS", userDetails);

                }
            }else {
                logService.logError("Empty user");
                userDetails.add(new UserResponse.UserDetail(0, true, "User must not be null"));

            }





        } catch (Exception e) {
            logService.logError("Service Error");
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

        //empty user control
        if (updateUser.getUsername() == null || updateUser.getUsername().trim().isEmpty()) {
            logService.logError("Empty user ");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User must not be null"));
            return new UserResponse(userDetails);
        }

        if (existingUser.isEmpty()) {

            logService.logError("User not found : " + updateUser.getUsername());
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User not found"));
            return new UserResponse(userDetails); // Hata mesajı ile yanıt döndür
        }
        try{
            User user=existingUser.get();
            if(existingUser.isEmpty()) {
                logService.logError("User must not be null : " + updateUser.getUsername());
                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_FAILURE: User must not be null"));
            }else {


                user.setUsername(updateUser.getUsername());
                user.setPassword(updateUser.getPassword());
                user.setEmail(updateUser.getEmail());
              //  userRepository.save(user);
                logService.logInfo("User updated successfully: " + user.getUsername());
              //  userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
                UserResponse response = GenericServiceHelper.saveEntity(user, userRepository,
                        "SERVICE_RESPONSE_SUCCESS", userDetails);


            }



        }catch (Exception e){
            logService.logError("Service Error ");
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