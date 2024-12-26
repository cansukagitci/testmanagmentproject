package com.example.testmanagment.service;

import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.User;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.repository.ProjectRepository;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    public ProjectRepository projectRepository;

    //define jwt
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LogService logService;

    @Autowired
    private UserRepository userRepository;

    ////////////////////////////////////////////////////////////////////
    //add project

    public UserResponse addProject(ProjectDto projectDto){

        Optional<User> optionalUser = userRepository.findById(projectDto.getUserId());

        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        if (optionalUser.isEmpty()) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User not found"));
            return new UserResponse(userDetails);
        }

        // Kullanıcıyı al
        User user = optionalUser.get();
        System.out.println("User ID: " + user.getId());

        // Yeni proje nesnesi oluştur
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());

        project.setUser(user); // Kullanıcıyı ata
        project.setLabel(projectDto.getLabel());



        projectRepository.save(project);
        userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
        return new UserResponse(userDetails);




    }
}
