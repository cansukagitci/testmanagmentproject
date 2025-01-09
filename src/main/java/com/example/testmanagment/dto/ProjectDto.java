package com.example.testmanagment.dto;

import com.example.testmanagment.model.Label;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class ProjectDto {
    private Long id;
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


}
