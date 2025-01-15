package com.example.testmanagment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "ref_test_to_project")
@Getter
@Setter
@ToString
@IdClass(TesttoProject.class)
public class TesttoProject {
    @Id
    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "id",nullable = false)
    private Project project;
    @Id
    @ManyToOne
    @JoinColumn(name = "test_id",referencedColumnName = "id",nullable = false)
    private Test test;

    public TesttoProject(){}

    public TesttoProject(Project project, Test test) {
        this.project = project;
        this.test = test;
    }
}
