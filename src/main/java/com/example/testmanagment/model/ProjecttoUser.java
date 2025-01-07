package com.example.testmanagment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ref_project_to_users")
@IdClass(ProjecttoUser.class)
@Getter
@Setter
@ToString
public class ProjecttoUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "projectid",referencedColumnName = "id",nullable = false)
    private Project project;
    @Id
    @ManyToOne
    @JoinColumn(name = "userid",referencedColumnName = "id",nullable = false)
    private User user;

    public ProjecttoUser(){}

    public ProjecttoUser(Project project, User user) {
        this.project = project;
        this.user = user;
    }
}
