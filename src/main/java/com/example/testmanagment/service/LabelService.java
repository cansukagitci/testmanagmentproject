package com.example.testmanagment.service;

import com.example.testmanagment.dto.LabelDto;
import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.model.Label;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.User;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.repository.LabelRepository;
import com.example.testmanagment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LabelService {
    @Autowired
    public LabelRepository labelRepository;

    //define jwt
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LogService logService;

    /////////////////////////////////////////////////
    //add label
    public UserResponse addLabel(LabelDto labelDto) {


        List<UserResponse.UserDetail> userDetails = new ArrayList<>();



        //control duplicate
        Label existingLabel = labelRepository.findByLabelName(labelDto.getLabelname());
        if (existingLabel != null) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Label name already exists"));
            return new UserResponse(userDetails);
        }




        Label label = new Label();
        label.setLabelName(labelDto.getLabelname());
        label.setLabelDescription(labelDto.getLabeldescription());
        label.setLabelColor(labelDto.getLabelcolor());

        try {
            labelRepository.save(label);
            logService.logInfo("Label added successfully: " + label.getLabelName());
            userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
        }catch (Exception e){
            logService.logError("Service Error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));

        }
        return new UserResponse(userDetails);

    }


}
