package com.example.testmanagment.service;

import com.example.testmanagment.dto.IssuesDTO;
import com.example.testmanagment.dto.TestDto;
import com.example.testmanagment.helper.GenericServiceHelper;
import com.example.testmanagment.model.Issues;
import com.example.testmanagment.model.Test;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.repository.IssuesRepository;
import com.example.testmanagment.repository.ProjectRepository;
import com.example.testmanagment.repository.TestRepository;
import com.example.testmanagment.repository.TesttoProjectRepository;
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

    //define jwt
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LogService logService;

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

        issue.setIs_deleted(true);


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

}
