package com.example.testmanagment.dto;

import java.util.List;

public class ProjecttoLabelDTO {
    private Long projectId;

    private List<Long> labelId;

    public ProjecttoLabelDTO(){}

    public ProjecttoLabelDTO(Long projectId, List<Long> labelIds) {
        this.projectId = projectId;
        this.labelId = labelId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<Long> getLabelId() {
        return labelId;
    }

    public void setLabelId(List<Long> labelId) {
        this.labelId = labelId;
    }
}