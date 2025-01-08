package com.example.testmanagment.service;

import com.example.testmanagment.dto.LabelDto;
import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.helper.GenericServiceHelper;
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
        Label existingLabel = labelRepository.findByLabelname(labelDto.getLabelname());
        if (existingLabel != null) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Label name already exists"));
            return new UserResponse(userDetails);
        }


        Label label = new Label();
        label.setLabelname(labelDto.getLabelname());
        label.setLabeldescription(labelDto.getLabeldescription());
        label.setLabelcolor(labelDto.getLabelcolor());

        label.setIsdeleted(false);

        try {
            //labelRepository.save(label);
            logService.logInfo("Label added successfully: " + label.getLabelname());
           // userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));

            UserResponse response = GenericServiceHelper.saveEntity(label, labelRepository,
                    "Added label successfully", userDetails);
        } catch (Exception e) {
            logService.logError("Service Error");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));

        }
        return new UserResponse(userDetails);

    }

    ////////////////////////////////////////////////////////////////////
    //delete label
    public UserResponse deleteLabel(long labelId) {
        Optional<Label> deleteLabel = labelRepository.findById(labelId);
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();
        deleteLabel.ifPresentOrElse(label -> {
            // Check if the label is already marked as deleted
            if (label.getIsdeleted()) {
                logService.logError("Label is already deleted: " + label.getLabelname());
                userDetails.add(new UserResponse.UserDetail(0, true, "Label already is deleted"));
            } else {
                // Mark the label as deleted
                label.setIsdeleted(true);
             //   labelRepository.save(label);
                logService.logInfo("Label deleted successfully: " + label.getLabelname());
               // userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
                UserResponse response = GenericServiceHelper.saveEntity(label, labelRepository,
                        "Deleted label successfully", userDetails);
            }
        }, () -> {
            // Handle case when the label is not found
            logService.logError("Label not found for ID: " + labelId);
            userDetails.add(new UserResponse.UserDetail(0, false, "Label not found")); // Better error message
        });

        return new UserResponse(userDetails);
    }

    //update label
    public UserResponse updateLabel(Long id, Label updateLabel) {
        List<UserResponse.UserDetail> userDetails = new ArrayList<>();


        // Proje adı boş kontrolü
        if (updateLabel.getLabelname() == null || updateLabel.getLabelname().trim().isEmpty()) {

            logService.logError("Empty label name");
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Label name must not be null"));
            return new UserResponse(userDetails);
        }


        Optional<Label> existingLabel = labelRepository.findById(id);

        // Proje mevcut değilse
        if (existingLabel.isEmpty()) {
            logService.logError("Project not found for ID: " + id);
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: Project not found"));
            return new UserResponse(userDetails);
        }


        Label label = existingLabel.orElseThrow(() -> {
            logService.logError("Label  must not be null: " + id);
            return new IllegalArgumentException("SERVICE_RESPONSE_FAILURE: Label must not be null");
        });


        label.setLabelname(updateLabel.getLabelname());
        label.setLabeldescription(updateLabel.getLabeldescription());

       // labelRepository.save(label);

        logService.logInfo("Project updated successfully: " + label.getLabelname());
       // userDetails.add(new UserResponse.UserDetail(0, true, "SERVICE_RESPONSE_SUCCESS"));
        UserResponse response = GenericServiceHelper.saveEntity(label, labelRepository,
                "Updated label successfully", userDetails);


        return new UserResponse(userDetails);
    }

    //get Label
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    //get Label by Id
    public Optional<Label> getLabelById(Long id) {

        return labelRepository.findById(id);  // Kullanıcıyı ID ile bulma
    }


}
