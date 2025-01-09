package com.example.testmanagment.service;

import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.dto.ProjecttoUserDTO;
import com.example.testmanagment.exception.CustomException;
import com.example.testmanagment.helper.GenericServiceHelper;
import com.example.testmanagment.model.*;
import com.example.testmanagment.repository.*;
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

    @Autowired
    private ProjecttoUserRepository projectToUserRepository;

    //define interface for repository
    public interface RelationshipRepository<T, U> {
        Optional<T> findByEntity1AndEntity2(U entity1, U entity2);
    }

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

    public UserResponse addProject(ProjectDto projectDto) {

        // Optional<Label> optionalLabel = labelRepository.findById(projectDto.getLabelId());
        Set<Label> labels = new HashSet<>();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        //control duplicate
        Project existingProject = projectRepository.findByName(projectDto.getName());
        if (existingProject != null) {
            logService.logError("Project name already exists for this user: " + projectDto.getName());
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Project name already exists for this user"));
            return new UserResponse(userDetails);
        }


        if (projectDto.getName() == null || projectDto.getName().trim().isEmpty()) {
            logService.logError("Name cannot be empty");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Name cannot be empty"));
            return new UserResponse(userDetails);
        }


        // create project object
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());


        try {
           // projectRepository.save(project);
            logService.logInfo("Project added successfully: " + project.getName());
           // userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
            UserResponse response = GenericServiceHelper.saveEntity(project, projectRepository,
                    "Added project successfully", userDetails);

        } catch (Exception e) {
            //logService.logError("Service Error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));

        }
        return new UserResponse(userDetails);


    }

    ////////////////////////////////////////////////////////////////////
//delete project
    public UserResponse deleteProject(long projectId) {
        Optional<Project> deleteProject = projectRepository.findById(projectId);
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        // Handle the presence or absence of the project
        deleteProject.ifPresentOrElse(project -> {
            // Check if the project is already marked as deleted
            if (project.isIsdeleted()) {
                logService.logError("Project is already deleted: " + project.getName());
                userDetails.add(new UserResponse.UserDetail(0, true, "Project already is deleted"));
            } else {
                // Mark the project as deleted
                project.setIsdeleted(true);
               // projectRepository.save(project);
                logService.logInfo("Project deleted successfully: " + project.getName());
               // userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));

                UserResponse response = GenericServiceHelper.saveEntity(project, projectRepository,
                        "Deleted project successfully", userDetails);
            }
        }, () -> {
            // Handle case when the project is not found
            logService.logError("Project not found for ID: " + projectId);
            userDetails.add(new UserResponse.UserDetail(0, false, "Project not found")); // Better error message
        });
        return new UserResponse(userDetails);
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //update project
    public UserResponse updateProject(Long projectid, Project updateProject) {
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        // Proje adı boş kontrolü
        if (updateProject.getName() == null || updateProject.getName().trim().isEmpty()) {
            logService.logError("Empty project name");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Project name must not be null"));
            return new UserResponse(userDetails);
        }

        // Projeyi bulma
        Optional<Project> existingProject = projectRepository.findById(projectid);

        // Proje mevcut değilse
        if (existingProject.isEmpty()) {
            logService.logError("Project not found for ID: " + projectid);
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Project not found"));
            return new UserResponse(userDetails);
        }


        Project project = existingProject.orElseThrow(() -> {
            logService.logError("Project must not be null: " + projectid);
            return new IllegalArgumentException("SERVICE_RESPONSE_FAILURE: Project must not be null");
        });

        // Proje silinmişse
        if (project.isIsdeleted()) {
            logService.logError("Bu proje zaten silinmiş: " + project.getName());
            userDetails.add(new UserResponse.UserDetail(0, false, "Project is already deleted."));
            return new UserResponse(userDetails);
        }

        // Güncellemeleri yap
        project.setName(updateProject.getName());
        project.setDescription(updateProject.getDescription());

        /*projectRepository.save(project);

        logService.logInfo("Project updated successfully: " + project.getName());


        userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));*/
        UserResponse response = GenericServiceHelper.saveEntity(project, projectRepository,
                "Updated project successfully", userDetails);


        return new UserResponse(userDetails);
    }
    ///////////////////////////////////

    private boolean isDuplicateEntry(Project project, Label label) {
        Optional<ProjecttoLabel> existingRelation = projectToLabelRepository.findByProjectAndLabel(project, label);
        return existingRelation.isPresent();
    }
///////////////////////////////////////////////////////////////////////
//assign label to project

    public UserResponse assignPTL(ProjecttoLabelDTO projectToLabelDto) {
        List<Long> labelIds = projectToLabelDto.getLabelId();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        // Projeyi kontrol et
        Project project;
        try {
            project = projectRepository.findById(projectToLabelDto.getProjectId())
                    .orElseThrow(() -> {
                        String errorMsg = "Project not found; ID: " + projectToLabelDto.getProjectId();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg); // Hata durumu için bir istisna fırlat
                    });
        } catch (RuntimeException e) {
            logService.logError("Service error");
            // İşlemi yapmadan önce kullanıcı detaylarını ekle
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails); // Hata ile geri dön
        }

        // İlişki oluşturma işlemi
        for (Long labelId : labelIds) {
            Label label;
            try {
                label = labelRepository.findById(labelId)
                        .orElseThrow(() -> {
                            String errorMsg = "Label not found; ID: " + labelId;
                            logService.logError(errorMsg);
                            return new RuntimeException(errorMsg); // Hata durumu için bir istisna fırlat
                        });
            } catch (RuntimeException e) {
                logService.logError("Service error");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
                return new UserResponse(userDetails); // Hata ile geri dön
            }

            Optional<ProjecttoLabel> existingRelation = projectToLabelRepository.findByProjectAndLabel(project, label);

            if (isDuplicateEntry(project, label)) {
                logService.logError("Duplicate entry and label for project ");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Duplicate entry for project " + project.getName() + " and label " + label.getLabelname()));
                continue; // Eğer duplicate varsa bu etiketi atla ve diğerlerini kontrol et
            }

            // İlişkiyi oluştur
            ProjecttoLabel ref = new ProjecttoLabel();
            ref.setProject(project);
            ref.setLabel(label);
            logService.logInfo("Label assigned to Project successfully");

           /* projectToLabelRepository.save(ref); // İlişkiyi kaydet
            userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));*/

            UserResponse response = GenericServiceHelper.saveEntity(ref, projectToLabelRepository,
                    "Label assigned to project successfully", userDetails);

        }

        return new UserResponse(userDetails);
    }


    //get projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    //get project by Id
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);  // Kullanıcıyı ID ile bulma
    }


    public UserResponse removePTL(ProjecttoLabelDTO projectToLabelDto) {
        List<Long> labelIds = projectToLabelDto.getLabelId();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        // Projeyi kontrol et
        Project project;
        try {
            project = projectRepository.findById(projectToLabelDto.getProjectId())
                    .orElseThrow(() -> {
                        String errorMsg = "Project not found; ID: " + projectToLabelDto.getProjectId();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg); // Hata durumu için bir istisna fırlat
                    });
        } catch (RuntimeException e) {
            logService.logError("Service error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails); // Hata ile geri dön
        }

        // İlişki kaldırma işlemi
        for (Long labelId : labelIds) {
            Label label;
            try {
                label = labelRepository.findById(labelId)
                        .orElseThrow(() -> {
                            String errorMsg = "Label not found; ID: " + labelId;
                            logService.logError(errorMsg);
                            return new RuntimeException(errorMsg); // Hata durumu için bir istisna fırlat
                        });
            } catch (RuntimeException e) {
                logService.logError("Service error");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
                return new UserResponse(userDetails); // Hata ile geri dön
            }

            // Proje ve etiket arasındaki ilişkiyi bul
            Optional<ProjecttoLabel> existingRelation = projectToLabelRepository.findByProjectAndLabel(project, label);

            if (existingRelation.isPresent()) {
                // İlişkiyi sil
                projectToLabelRepository.delete(existingRelation.get());
                logService.logInfo("Label removed from project successfully");

                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS: Label removed from project " + project.getName()));



            } else {
                logService.logError("No existing relation for project: " + project.getName() + " and label: " + label.getLabelname());
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: No relation found for project " + project.getName() + " and label " + label.getLabelname()));
            }
        }

        return new UserResponse(userDetails);
    }


    //////////////////////////////////////////////////
    //Handle error for project and users
    private <T> T findByIdAndHandleError(Optional<T> optionalEntity, Long id, String entityName, List<UserResponse.UserDetail> userDetails) {
        return optionalEntity.orElseThrow(() -> {
            String errorMsg = entityName + " not found; ID: " + id;
            logService.logError(errorMsg);
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + errorMsg));
            return new RuntimeException(errorMsg);
        });
    }
    //////////////////////////////////
    //assign user to project
    public UserResponse assignPTU(ProjecttoUserDTO projectToUserDto) {
        List<Long> userIds = projectToUserDto.getUserId();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        Project project;
        try {
           /* project = projectRepository.findById(projectToUserDto.getProjectId())
                    .orElseThrow(() -> {
                        String errorMsg = "Project not found; ID: " + projectToUserDto.getProjectId();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg);
                    });*/
            Optional<Project> optionalProject = projectRepository.findById(projectToUserDto.getProjectId());
            project = findByIdAndHandleError(optionalProject, projectToUserDto.getProjectId(), "Project", userDetails);


        } catch (RuntimeException e) {
            logService.logError("Service error");

            //userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails);
        }


        // İlişki oluşturma işlemi
        for (Long userId : userIds) {


            User user;
            try {/*
                user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            String errorMsg = "User not found; ID: " + userId;
                            logService.logError(errorMsg);
                            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + errorMsg));
                            return new RuntimeException(errorMsg);
                        });*/
                Optional<User> optionalUser = userRepository.findById(userId);
                user = findByIdAndHandleError(optionalUser, userId, "User", userDetails);

            } catch (RuntimeException e) {
                logService.logError("Service error");
                //userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
                //return new UserResponse(userDetails);
                continue;
            }

            Optional<ProjecttoUser> existingRelation = projectToUserRepository.findByProjectAndUser(project, user);

            if (existingRelation.isPresent()) {
                logService.logError("Duplicate entry and user for project ");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Duplicate entry for project " + project.getName() + " and user " + user.getUsername()));
                continue;
            }


            ProjecttoUser ref = new ProjecttoUser();
            ref.setProject(project);
            ref.setUser(user);
            logService.logInfo("User assigned to Project successfully");

          //  projectToUserRepository.save(ref);
            //userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));

            UserResponse response = GenericServiceHelper.saveEntity(ref, projectToUserRepository,
                    "User assigned to project successfully", userDetails);
        }

        return new UserResponse(userDetails);
    }

    public UserResponse removePTU(ProjecttoUserDTO projectToUserDto) {
        List<Long> userIds = projectToUserDto.getUserId();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        // Projeyi kontrol et
        Project project;
        try {
            project = projectRepository.findById(projectToUserDto.getProjectId())
                    .orElseThrow(() -> {
                        String errorMsg = "Project not found; ID: " + projectToUserDto.getProjectId();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg); // Hata durumu için bir istisna fırlat
                    });
        } catch (RuntimeException e) {
            logService.logError("Service error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails); // Hata ile geri dön
        }

        // İlişki kaldırma işlemi
        for (Long userId : userIds) {
            User user;
            try {
                user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            String errorMsg = "User not found; ID: " + userId;
                            logService.logError(errorMsg);
                            return new RuntimeException(errorMsg); // Hata durumu için bir istisna fırlat
                        });
            } catch (RuntimeException e) {
                logService.logError("Service error");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
                return new UserResponse(userDetails); // Hata ile geri dön
            }

            // Proje ve etiket arasındaki ilişkiyi bul
            Optional<ProjecttoUser> existingRelation = projectToUserRepository.findByProjectAndUser(project, user);

            if (existingRelation.isPresent()) {
                // İlişkiyi sil
                projectToUserRepository.delete(existingRelation.get());
                logService.logInfo("User removed from project successfully");

                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS: User removed from project " + project.getName()));



            } else {
                logService.logError("No existing relation for project: " + project.getName() + " and user: " + user.getUsername());
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: No relation found for project " + project.getName() + " and label " + user.getUsername()));
            }
        }

        return new UserResponse(userDetails);
    }

}
