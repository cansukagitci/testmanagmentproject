package com.example.testmanagment.service;

import com.example.testmanagment.dto.*;
import com.example.testmanagment.helper.GenericServiceHelper;
import com.example.testmanagment.model.*;
import com.example.testmanagment.repository.*;
import com.example.testmanagment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IssuesService {
    @Autowired
    public IssuesRepository issuesRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private IssuetoLabelRepository issuetoLabelRepository;

    @Autowired
    private IssuetoUserRepository issuetoUserRepository;

    //define jwt
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LogService logService;

    @Autowired
    private UserRepository userRepository;

    //show error message
    private void addError(List<UserResponse.UserDetail> userDetails, String message) {
        logService.logError(message);
        userDetails.add(new UserResponse.UserDetail(0, false, message));
    }

    //create test
    public UserResponse addIssue(IssuesDTO issueDto) {

        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        if (issueDto.getIssue_item() == null || issueDto.getIssue_item().trim().isEmpty()) {
            addError(userDetails, "Issue cannot be empty");
            return new UserResponse(userDetails);
        }

        /////////////////////////////////////
        Issues issue=new Issues();
        issue.setIssue_title(issueDto.getIssue_title());
        issue.setIssue_type(issueDto.getIssue_type());
        issue.setDescription(issueDto.getDescription());
        issue.setIssue_item(issueDto.getIssue_item());
        issue.setDue_date(issueDto.getDue_date());

        issue.setIs_deleted(false);


        logService.logInfo("Issue added successfully: " + issue);
        UserResponse response = GenericServiceHelper.saveEntity(issue, issuesRepository,
                "Added issue successfully", userDetails);
        return new UserResponse(userDetails);
    }
    //get tests
    public List<Issues> getAllIssues() {
        return issuesRepository.findAll();
    }

    //get test by Id
    public Optional<Issues> getIssueById(Long id) {
        return issuesRepository.findById(id);
    }

    //delete test
    public UserResponse deleteIssue(Long id) {
        Optional<Issues> deleteIssue = issuesRepository.findById(id);
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        // Handle the presence or absence of the project
        deleteIssue.ifPresentOrElse(issue -> {
            // Check if the project is already marked as deleted
            if (issue.getIs_deleted()) {
                logService.logError("Issue is already deleted: " + issue.getId());
                userDetails.add(new UserResponse.UserDetail(0, true, "Issue already is deleted"));
            } else {
                // Mark the project as deleted
                issue.setIs_deleted(true);
                // projectRepository.save(project);
                logService.logInfo("Issue deleted successfully: " + issue.getId());
                // userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));

                UserResponse response = GenericServiceHelper.saveEntity(issue,issuesRepository,
                        "Deleted Issue successfully", userDetails);
            }
        }, () -> {
            // Handle case when the project is not found
            logService.logError("Issue not found for ID: " + id);
            userDetails.add(new UserResponse.UserDetail(0, false, "Issue not found")); // Better error message
        });
        return new UserResponse(userDetails);

    }


    //update
    public UserResponse updateIssue(Long id, IssuesDTO updateIssue) {
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        // Projeyi bulma
        Optional<Issues> existingIssue = issuesRepository.findById(id);

        // Proje mevcut değilse
        if (existingIssue.isEmpty()) {
            addError(userDetails, "Issue not found");
            return new UserResponse(userDetails);
        }

        if (updateIssue.getIssue_item() == null || updateIssue.getIssue_item().trim().isEmpty()) {
            logService.logError("Issue  cannot be empty");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE:Issue cannot be empty"));
            return new UserResponse(userDetails);
        }


        Issues issue = existingIssue.orElseThrow(() -> {
            logService.logError("Issue must not be null: " + id);
            return new IllegalArgumentException("SERVICE_RESPONSE_FAILURE: Issue must not be null");
        });

        // Proje silinmişse
        if (issue.getIs_deleted()) {
            logService.logError("Bu Issue zaten silinmiş: " + issue.getId());
            userDetails.add(new UserResponse.UserDetail(0, false, "Issue is already deleted."));
            return new UserResponse(userDetails);
        }

        // Güncellemeleri yap
        issue.setIssue_title(updateIssue.getIssue_title());
        issue.setIssue_type(updateIssue.getIssue_type());
        issue.setDescription(updateIssue.getDescription());
        issue.setIssue_item(updateIssue.getIssue_item());
        issue.setDue_date(updateIssue.getDue_date());





        logService.logInfo("Issue updated successfully: " + id);

        /*projectRepository.save(project);




        userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));*/
        UserResponse response = GenericServiceHelper.saveEntity(issue,issuesRepository,
                "Updated issue successfully", userDetails);


        return new UserResponse(userDetails);

    }


    //////////////////////////////////////////////////////

    public UserResponse assignITL(IssuetoLabelDTO issuetoLabelDTO) {


        List<UserResponse.UserDetail> userDetails = new ArrayList<>();
        List<Long> labelIds = issuetoLabelDTO.getLabelId();



        Issues issue;
        try {
            issue = issuesRepository.findById(issuetoLabelDTO.getIssue_Id())
                    .orElseThrow(() -> {
                        String errorMsg = "Issue not found; ID: " + issuetoLabelDTO.getIssue_Id();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg); // Hata durumu için bir istisna fırlat
                    });
        } catch (RuntimeException e) {
            logService.logError("Service error dfdsfsdf");
            // İşlemi yapmadan önce kullanıcı detaylarını ekle
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails); // Hata ile geri dön
        }



        // İlişki oluşturma işlemi
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
            Optional<IssuetoLabel> existingRelation = issuetoLabelRepository.findByIssueAndLabel(issue,label);

            if (existingRelation.isPresent()) {
                logService.logError("Duplicate entry ");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Duplicate entry for issue " + issue.getId() + " and label" + label.getId()));
                continue;
            }


            IssuetoLabel ref = new IssuetoLabel();
            ref.setIssue(issue);
            ref.setLabel(label);
            logService.logInfo("Label assigned to Issue successfully");



            UserResponse response = GenericServiceHelper.saveEntity(ref, issuetoLabelRepository,
                    "Test assigned to project successfully", userDetails);
        }
        return new UserResponse(userDetails);
    }

    /////////////////////////////////////////////////////////////////
    public UserResponse removeITL(IssuetoLabelDTO issuetoLabelDTO) {
        List<Long> labelIds=issuetoLabelDTO.getLabelId();

        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        Issues issue;
        try {
            issue = issuesRepository.findById(issuetoLabelDTO.getIssue_Id())
                    .orElseThrow(() -> {
                        String errorMsg = "Issue not found; ID: " + issuetoLabelDTO.getIssue_Id();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg);
                    });
        } catch (RuntimeException e) {
            logService.logError("Service error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails); // Hata ile geri dön
        }



        for (Long labelId : labelIds)
        {


            Label label;
            try {
                label =labelRepository.findById(labelId)
                        .orElseThrow(() -> {
                            String errorMsg = "Label not found; ID: " + labelId;
                            logService.logError(errorMsg);
                            return new RuntimeException(errorMsg);
                        });
            } catch (RuntimeException e) {
                logService.logError("Service error");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
                return new UserResponse(userDetails); // Hata ile geri dön
            }


            Optional<IssuetoLabel> existingRelation =issuetoLabelRepository.findByIssueAndLabel(issue,label);

            if (existingRelation.isPresent()) {

                issuetoLabelRepository.delete(existingRelation.get());
                logService.logInfo("Label removed from issue successfully");

                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS: Label removed from issue " + label.getLabelname()));


            } else {
                logService.logError("No existing relation for issue: " + issue.getIssue_item() + " and label: " + label.getLabelname());
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: No relation found for issue "+ " and label " + label.getLabelname()));
            }
        }

        return new UserResponse(userDetails);
    }

    public UserResponse assignITU(IssuetoUserDTO issuetoUserDTO) {


        List<UserResponse.UserDetail> userDetails = new ArrayList<>();
        List<Long> userIds = issuetoUserDTO.getUserId();



        Issues issue;
        try {
            issue = issuesRepository.findById(issuetoUserDTO.getIssue_Id())
                    .orElseThrow(() -> {
                        String errorMsg = "Issue not found; ID: " + issuetoUserDTO.getIssue_Id();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg); // Hata durumu için bir istisna fırlat
                    });
        } catch (RuntimeException e) {
            logService.logError("Service error dfdsfsdf");
            // İşlemi yapmadan önce kullanıcı detaylarını ekle
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails); // Hata ile geri dön
        }



        // İlişki oluşturma işlemi
        // İlişki oluşturma işlemi
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
            Optional<IssuetoUser> existingRelation = issuetoUserRepository.findByIssueAndUser(issue,user);

            if (existingRelation.isPresent()) {
                logService.logError("Duplicate entry ");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Duplicate entry for issue " + issue.getId() + " and user" + user.getId()));
                continue;
            }


            IssuetoUser ref = new IssuetoUser();
            ref.setIssue(issue);
            ref.setUser(user);
            logService.logInfo("User assigned to Issue successfully");



            UserResponse response = GenericServiceHelper.saveEntity(ref, issuetoUserRepository,
                    "User assigned to issue successfully", userDetails);
        }
        return new UserResponse(userDetails);
    }
    ////////////////////////////////////////////
    public UserResponse removeITU(IssuetoUserDTO issuetoUserDTO) {
        List<Long> userIds=issuetoUserDTO.getUserId();

        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        Issues issue;
        try {
            issue = issuesRepository.findById(issuetoUserDTO.getIssue_Id())
                    .orElseThrow(() -> {
                        String errorMsg = "Issue not found; ID: " + issuetoUserDTO.getIssue_Id();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg);
                    });
        } catch (RuntimeException e) {
            logService.logError("Service error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails); // Hata ile geri dön
        }



        for (Long userId : userIds)
        {



            User user;
            try {
                user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            String errorMsg = "User not found; ID: " + userId;
                            logService.logError(errorMsg);
                            return new RuntimeException(errorMsg);
                        });
            } catch (RuntimeException e) {
                logService.logError("Service error");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
                return new UserResponse(userDetails); // Hata ile geri dön
            }


            Optional<IssuetoUser> existingRelation =issuetoUserRepository.findByIssueAndUser(issue,user);

            if (existingRelation.isPresent()) {

                issuetoUserRepository.delete(existingRelation.get());
                logService.logInfo("user removed from issue successfully");

                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS: User removed from issue " + user.getUsername()));


            } else {
                logService.logError("No existing relation for issue: " + issue.getIssue_item() + " and user: " + user.getUsername());
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: No relation found for issue "+ " and user " + user.getUsername()));
            }
        }

        return new UserResponse(userDetails);
    }

}
