package com.example.testmanagment.dto;

import com.example.testmanagment.model.Label;

import java.util.HashSet;
import java.util.Set;

public class ProjectDto {

    private String name;
    private String description;
    private boolean isdeleted;

    // Default constructor
    public ProjectDto() {}

    public ProjectDto(String name, String description,  boolean isdeleted) {
        this.name = name;
        this.description = description;

        this.isdeleted = isdeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }
}
