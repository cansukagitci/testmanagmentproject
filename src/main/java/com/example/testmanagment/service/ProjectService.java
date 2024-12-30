package com.example.testmanagment.service;

import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.exception.CustomException;
import com.example.testmanagment.model.*;
import com.example.testmanagment.repository.LabelRepository;
import com.example.testmanagment.repository.ProjectRepository;
import com.example.testmanagment.repository.ProjectToLabelRepository;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.util.JwtUtil;
import jakarta.servlet.http.PushBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.testmanagment.dto.ProjecttoLabelDTO;

import java.util.*;

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


    @Autowired
    private LabelRepository labelRepository;


    @Autowired
    private ProjectToLabelRepository projectToLabelRepository;

    private void validateLabel(Long labelId) {
        Optional<Label> optionalLabel = labelRepository.findById(labelId);
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        if (!optionalLabel.isPresent()) {

          //  throw new CustomException("Label not found; ID: " + labelId);
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Label not found"));

        }
    }


    ////////////////////////////////////////////////////////////////////
    //add project

    public UserResponse addProject(ProjectDto projectDto){

       // Optional<Label> optionalLabel = labelRepository.findById(projectDto.getLabelId());
        Set<Label> labels = new HashSet<>();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();





        //control duplicate
        Project existingProject = projectRepository.findByName(projectDto.getName());
        if (existingProject != null) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Project name already exists for this user"));
            return new UserResponse(userDetails);
        }



        if (projectDto.getName() == null || projectDto.getName().trim().isEmpty()) {

            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Name cannot be empty"));
            return new UserResponse(userDetails);
        }









        // create project object
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());







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

///////////////////////////////////////////////////////////////////////
//assign label to project

  /*  public UserResponse assignPTL(long projectId,long labelId){
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        if (!optionalProject.isPresent()) {
            throw new RuntimeException("Project not found; ID: " + projectId);
        }

        Optional<Label> optionalLabel = labelRepository.findById(labelId);
        if (!optionalLabel.isPresent()) {
            throw new RuntimeException("Label not found; ID: " + labelId);
        }

        ProjecttoLabel ref=new ProjecttoLabel();

        ref.setProject(optionalProject.get());
        ref.setLabel(optionalLabel.get());

        System.out.println(optionalProject.get());


        try {
            projectToLabelRepository.save(ref);
            logService.logInfo("Assign Label added successfully: ");
            userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
        }catch (Exception e){
            logService.logError("Service Error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));

        }
        return new UserResponse(userDetails);
    }*/

    public UserResponse assignPTL(ProjecttoLabelDTO projectToLabelDto) {
        List<Long> labelIds = projectToLabelDto.getLabelId();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        // Projeyi kontrol et
        Optional<Project> optionalProject = projectRepository.findById(projectToLabelDto.getProjectId());
        if (!optionalProject.isPresent()) {
            throw new RuntimeException("Project not found; ID: " + projectToLabelDto.getProjectId());
        }

        // İlişki oluşturma işlemi
        for (Long labelId : labelIds) {
            Optional<Label> optionalLabel = labelRepository.findById(labelId);
            if (!optionalLabel.isPresent()) {
                throw new RuntimeException("Label not found; ID: " + labelId);
            }


            ProjecttoLabel ref = new ProjecttoLabel();
            ref.setProject(optionalProject.get());
            ref.setLabel(optionalLabel.get());

            projectToLabelRepository.save(ref); // İlişkiyi kaydet
            userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
        }

        return new UserResponse(userDetails);

    }


}
