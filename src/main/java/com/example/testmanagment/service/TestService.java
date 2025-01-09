package com.example.testmanagment.service;

import com.example.testmanagment.dto.TestDto;
import com.example.testmanagment.helper.GenericServiceHelper;
import com.example.testmanagment.model.Test;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public UserResponse addTest(TestDto testDto){

        List<UserResponse.UserDetail> userDetails = new ArrayList<>();
        Test test=new Test();
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

}
