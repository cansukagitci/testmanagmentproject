package com.example.testmanagment.service;

import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.User;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.repository.ProjectRepository;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.util.JwtUtil;
import jakarta.servlet.http.PushBuilder;
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

       //user not found
        if (optionalUser.isEmpty()) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User not found"));
            return new UserResponse(userDetails);
        }

        //control duplicate
        Project existingProject = projectRepository.findByNameAndUserId(projectDto.getName(), projectDto.getUserId());
        if (existingProject != null) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Project name already exists for this user"));
            return new UserResponse(userDetails);
        }



        if (projectDto.getName() == null || projectDto.getName().trim().isEmpty()) {

            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Name cannot be empty"));
            return new UserResponse(userDetails);
        }
       System.out.println(projectDto.getUserId());
        if(projectDto.getUserId() == null){

            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: User id must not be null"));
            return new UserResponse(userDetails);
        }





        User user = optionalUser.get();


        // create project object
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setUser(user);
        project.setLabel(projectDto.getLabel());



        try {
            projectRepository.save(project);
            logService.logInfo("Project added successfully: " + project.getName());
            userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
        }catch (Exception e){
            logService.logError("Service Error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));

        }
        return new UserResponse(userDetails);


    }

////////////////////////////////////////////////////////////////////
//delete user
public UserResponse deleteProject(long projectId) {
    Optional<Project> deleteProject =projectRepository.findById(projectId);
    List<UserResponse.UserDetail> userDetails = new ArrayList<>();




    try {
        Project project=deleteProject.get();
        if(project != null)
        {
            if(project.isIsdeleted()){
                logService.logError("Project isalready deleted " + project.getName());
                userDetails.add(new UserResponse.UserDetail(0, true, "Project already is deleted"));
            }else {
                project.setIsdeleted(true);
                logService.logInfo("Project deleted successfully: " + project.getName());
                projectRepository.save(project);

                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));

            }
        }else {
            logService.logError("Empty user");
            userDetails.add(new UserResponse.UserDetail(0, true, "Project must not be null"));

        }





    } catch (Exception e) {
        logService.logError("Service Error");
        userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
    }
    return new UserResponse(userDetails);
}





}
