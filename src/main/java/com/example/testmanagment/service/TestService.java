package com.example.testmanagment.service;

import com.example.testmanagment.dto.TestDto;
import com.example.testmanagment.helper.GenericServiceHelper;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.Test;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private LogService logService;

    public UserResponse addTest(TestDto testDto) {

        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        if (testDto.getTest_item() == null || testDto.getTest_item().trim().isEmpty()) {
            logService.logError("Test Scenerio cannot be empty");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Test Scenerio Name cannot be empty"));
            return new UserResponse(userDetails);
        }

        if (testDto.getResult() == null || testDto.getResult().trim().isEmpty()) {
            logService.logError("Test Result cannot be empty");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Test Result cannot be empty"));
            return new UserResponse(userDetails);
        }


        Test test = new Test();
        test.setPre_condition(testDto.getPre_condition());
        test.setAssumptions(testDto.getAssumptions());
        test.setTest_input(testDto.getTest_input());
        test.setExpected_test_result(testDto.getExpected_test_result());
        test.setIsdeleted(testDto.isIsdeleted());
        test.setTest_item(testDto.getTest_item());
        test.setDescription(testDto.getDescription());
        test.setResult(testDto.getResult());


        UserResponse response = GenericServiceHelper.saveEntity(test, testRepository,
                "Added test successfully", userDetails);
        return new UserResponse(userDetails);
    }

    public UserResponse deleteTest(Long testid) {
        Optional<Test> deleteTest = testRepository.findById(testid);
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        // Handle the presence or absence of the project
        deleteTest.ifPresentOrElse(test -> {
            // Check if the project is already marked as deleted
            if (test.isIsdeleted()) {
                logService.logError("Test is already deleted: " + test.getId());
                userDetails.add(new UserResponse.UserDetail(0, true, "Test already is deleted"));
            } else {
                // Mark the project as deleted
                test.setIsdeleted(true);
                // projectRepository.save(project);
                logService.logInfo("Test deleted successfully: " + test.getId());
                // userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));

                UserResponse response = GenericServiceHelper.saveEntity(test, testRepository,
                        "Deleted test successfully", userDetails);
            }
        }, () -> {
            // Handle case when the project is not found
            logService.logError("Test not found for ID: " + testid);
            userDetails.add(new UserResponse.UserDetail(0, false, "Test not found")); // Better error message
        });
        return new UserResponse(userDetails);

    }

    //update
    public UserResponse updateTest(Long testid, TestDto updateTest) {
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        // Projeyi bulma
        Optional<Test> existingTest = testRepository.findById(testid);

        // Proje mevcut değilse
        if (existingTest.isEmpty()) {
            logService.logError("Test not found for ID: " + testid);
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Test not found"));
            return new UserResponse(userDetails);
        }

        if (updateTest.getResult() == null || updateTest.getResult().trim().isEmpty()) {
            logService.logError("Test Result cannot be empty");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Test Result cannot be empty"));
            return new UserResponse(userDetails);
        }


        Test test = existingTest.orElseThrow(() -> {
            logService.logError("Test must not be null: " + testid);
            return new IllegalArgumentException("SERVICE_RESPONSE_FAILURE: Test must not be null");
        });

        // Proje silinmişse
        if (test.isIsdeleted()) {
            logService.logError("Bu Test zaten silinmiş: " + test.getId());
            userDetails.add(new UserResponse.UserDetail(0, false, "Test is already deleted."));
            return new UserResponse(userDetails);
        }

        // Güncellemeleri yap

        test.setPre_condition(updateTest.getPre_condition());
        test.setAssumptions(updateTest.getAssumptions());
        test.setTest_input(updateTest.getTest_input());
        test.setExpected_test_result(updateTest.getExpected_test_result());
        test.setIsdeleted(updateTest.isIsdeleted());
        test.setTest_item(updateTest.getTest_item());
        test.setDescription(updateTest.getDescription());
        test.setResult(updateTest.getResult());




        /*projectRepository.save(project);

        logService.logInfo("Project updated successfully: " + project.getName());


        userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));*/
        UserResponse response = GenericServiceHelper.saveEntity(test, testRepository,
                "Updated test successfully", userDetails);


        return new UserResponse(userDetails);

    }

    //get tests
    public List<Test> getAllTest() {
        return testRepository.findAll();
    }

    //get test by Id
    public Optional<Test> getTestById(Long id) {
        return testRepository.findById(id);
    }

}