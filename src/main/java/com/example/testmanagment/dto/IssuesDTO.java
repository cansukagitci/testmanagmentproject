package com.example.testmanagment.dto;

import lombok.Getter;
import lombok.Setter;

import java.security.PublicKey;
import java.util.Date;

@Getter
@Setter
public class IssuesDTO {
    private Long id;
    private String issue_title;
    private String issue_type;
    private String description;
    private String issue_item;
    private Date due_date;
    private Boolean is_deleted;

    public IssuesDTO(){}

    public IssuesDTO(String issue_title, String issue_type, String description, String issue_item, Date due_date, Boolean is_deleted) {
        this.issue_title = issue_title;
        this.issue_type = issue_type;
        this.description = description;
        this.issue_item = issue_item;
        this.due_date = due_date;
        this.is_deleted = is_deleted;
    }
}
