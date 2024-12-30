package com.example.testmanagment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ref_project_to_label")
@IdClass(ProjecttoLabel.class)
public class ProjecttoLabel {
    @Id
    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "id",nullable = false)
    private Project project;
    @Id
    @ManyToOne
    @JoinColumn(name = "label_id",referencedColumnName = "id",nullable = false)
    private Label label;

    public ProjecttoLabel(){}

    public ProjecttoLabel(Project project, Label label) {
        this.project = project;
        this.label = label;
    }

    //getter setter


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    //tostring

    @Override
    public String toString() {
        return "ProjecttoLabel{" +
                ", project=" + project +
                ", label=" + label +
                '}';
    }
}
