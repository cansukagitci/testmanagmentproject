package com.example.testmanagment.service;

import com.example.testmanagment.dto.ProjecttoLabelDTO;
import com.example.testmanagment.dto.ProjecttoUserDTO;
import com.example.testmanagment.dto.TestDto;
import com.example.testmanagment.dto.TesttoProjectDTO;
import com.example.testmanagment.helper.GenericServiceHelper;
import com.example.testmanagment.model.*;
import com.example.testmanagment.repository.ProjectRepository;
import com.example.testmanagment.repository.TestRepository;
import com.example.testmanagment.repository.TesttoProjectRepository;
import jakarta.servlet.http.PushBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {


    private final TestRepository testRepository;
    private final LogService logService;
    private final TesttoProjectRepository testToProjectRepository;
    private final ProjectRepository projectRepository;

    //constructor injection
    @Autowired
    public TestService(TestRepository testRepository, LogService logService,TesttoProjectRepository testToProjectRepository,ProjectRepository projectRepository) {
        this.testRepository = testRepository;
        this.logService = logService;
        this.testToProjectRepository=testToProjectRepository;
        this.projectRepository=projectRepository;
    }

    //show error message
    private void addError(List<UserResponse.UserDetail> userDetails, String message) {
        logService.logError(message);
        userDetails.add(new UserResponse.UserDetail(0, false, message));
    }

    //create test
    public UserResponse addTest(TestDto testDto) {

        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        if (testDto.getTest_item() == null || testDto.getTest_item().trim().isEmpty()) {
            addError(userDetails, "Test Scenerio Name cannot be empty");
            return new UserResponse(userDetails);
        }

        if (testDto.getResult() == null || testDto.getResult().trim().isEmpty()) {
            addError(userDetails, "Test Result cannot be empty");
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

        logService.logInfo("Test added successfully: " + test);
        UserResponse response = GenericServiceHelper.saveEntity(test, testRepository,
                "Added test successfully", userDetails);
        return new UserResponse(userDetails);
    }

    //delete test
    public UserResponse deleteTest(Long id) {
        Optional<Test> deleteTest = testRepository.findById(id);
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
            logService.logError("Test not found for ID: " + id);
            userDetails.add(new UserResponse.UserDetail(0, false, "Test not found")); // Better error message
        });
        return new UserResponse(userDetails);

    }

    //update
    public UserResponse updateTest(Long id, TestDto updateTest) {
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        // Projeyi bulma
        Optional<Test> existingTest = testRepository.findById(id);

        // Proje mevcut değilse
        if (existingTest.isEmpty()) {
            addError(userDetails, "Test not found");
            return new UserResponse(userDetails);
        }

        if (updateTest.getResult() == null || updateTest.getResult().trim().isEmpty()) {
            logService.logError("Test Result cannot be empty");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Test Result cannot be empty"));
            return new UserResponse(userDetails);
        }


        Test test = existingTest.orElseThrow(() -> {
            logService.logError("Test must not be null: " + id);
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


        logService.logInfo("Project updated successfully: " + id);

        /*projectRepository.save(project);




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
    //////////////////////
    //Handle error for project and users
    private <T> T findByIdAndHandleError(Optional<T> optionalEntity, Long id, String entityName, List<UserResponse.UserDetail> userDetails) {
        return optionalEntity.orElseThrow(() -> {
            String errorMsg = entityName + " not found; ID: " + id;
            logService.logError(errorMsg);
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + errorMsg));
            return new RuntimeException(errorMsg);
        });
    }
    /////////////////////////////////////

    public UserResponse assignTTP(TesttoProjectDTO testtoProjectDTO) {

        List<Long> testIds = testtoProjectDTO.getTestId();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        Project project;
        try {
            project = projectRepository.findById(testtoProjectDTO.getProjectid())
                    .orElseThrow(() -> {
                        String errorMsg = "Project not found; ID: " + testtoProjectDTO.getTestId();
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
        for (Long testId : testIds) {



            Test test;
            try {/*
                user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            String errorMsg = "User not found; ID: " + userId;
                            logService.logError(errorMsg);
                            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + errorMsg));
                            return new RuntimeException(errorMsg);
                        });*/

                Optional<Test> optionalTest = testRepository.findById(testId);

                test = findByIdAndHandleError(optionalTest, testId, "Test", userDetails);

            } catch (RuntimeException e) {
                logService.logError("Service error test");
                //userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
                //return new UserResponse(userDetails);
                continue;
            }

            Optional<TesttoProject> existingRelation = testToProjectRepository.findByProjectAndTest(project,test);

            if (existingRelation.isPresent()) {
                logService.logError("Duplicate entry and test for project ");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Duplicate entry for project " + project.getName() + " and test" + test.getId()));
                continue;
            }


            TesttoProject ref = new TesttoProject();
            ref.setTest(test);
            ref.setProject(project);
            logService.logInfo("Test assigned to Project successfully");



            UserResponse response = GenericServiceHelper.saveEntity(ref, testToProjectRepository,
                    "Test assigned to project successfully", userDetails);
        }
        return new UserResponse(userDetails);
    }


    /////////////////////////////////////////////////////////////////
    public UserResponse removeTTP(TesttoProjectDTO testtoProjectDTO) {
        List<Long> testIds = testtoProjectDTO.getTestId();
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();

        // Projeyi kontrol et
        Project project;
        try {
            project = projectRepository.findById(testtoProjectDTO.getProjectid())
                    .orElseThrow(() -> {
                        String errorMsg = "Project not found; ID: " + testtoProjectDTO.getProjectid();
                        logService.logError(errorMsg);
                        return new RuntimeException(errorMsg);
                    });
        } catch (RuntimeException e) {
            logService.logError("Service error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
            return new UserResponse(userDetails); // Hata ile geri dön
        }



        for (Long testId : testIds)
        {

            Test test;
            try {
                test =testRepository.findById(testId)
                        .orElseThrow(() -> {
                            String errorMsg = "Test not found; ID: " + testId;
                            logService.logError(errorMsg);
                            return new RuntimeException(errorMsg);
                        });
            } catch (RuntimeException e) {
                logService.logError("Service error");
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
                return new UserResponse(userDetails); // Hata ile geri dön
            }


            Optional<TesttoProject> existingRelation = testToProjectRepository.findByProjectAndTest(project, test);

            if (existingRelation.isPresent()) {

                testToProjectRepository.delete(existingRelation.get());
                logService.logInfo("Test removed from project successfully");

                userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS: Test removed from project " + project.getName()));


            } else {
                logService.logError("No existing relation for project: " + project.getName() + " and test: " + test.getId());
                userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: No relation found for project " + project.getName() + " and test " + test.getId()));
            }
        }

        return new UserResponse(userDetails);
    }

    //////////////////////////////////////////

    public List<Test> getDeletedTests() {
        return testRepository.findByIsdeleted(true);
    }

    public List<Test> getActiveTests() {
        return testRepository.findByIsdeletedFalse();
    }


    public double calculateSuccessRate() {
        long passedTests = testRepository.countPassedTests();
        long totalTests = testRepository.countTotalTests();

        if (totalTests == 0) {
            return 0;
        }

        return (double) passedTests / totalTests * 100; // Yüzde olarak başarı oranı
    }

    public double calculateFailedRate() {
        long failedTests = testRepository.countFiledTests();
        long totalTests = testRepository.countTotalTests();

        if (totalTests == 0) {
            return 0;
        }

        return (double) failedTests / totalTests * 100; //
    }

}