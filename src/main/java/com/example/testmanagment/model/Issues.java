package com.example.testmanagment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name="issues")
public class Issues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="issue_title")
    private String issue_title;

    @Column(name= "issue_type")
    private String issue_type;

    @Column(name= "description")
    private String description;

    @Column(name= "issue_item")
    private String issue_item;

    @Column(name= "due_date")
    private Date due_date;

    @Column(name="is_deleted")
    private Boolean is_deleted;

    public Issues(){}

    public Issues(String issue_title, String issue_type, String description, String issue_item, Date due_date, Boolean is_deleted) {
        this.issue_title = issue_title;
        this.issue_type = issue_type;
        this.description = description;
        this.issue_item = issue_item;
        this.due_date = due_date;
        this.is_deleted = is_deleted;
    }
}
