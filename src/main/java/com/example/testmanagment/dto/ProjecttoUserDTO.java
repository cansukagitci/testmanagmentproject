package com.example.testmanagment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ProjecttoUserDTO {
    private Long projectId;

    private List<Long> userId;

    public ProjecttoUserDTO(){}

    public ProjecttoUserDTO(Long projectId, List<Long> userId) {
        this.projectId = projectId;
        this.userId = userId;
    }
}
