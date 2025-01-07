package com.example.testmanagment.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@ToString
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;
    @Column(name="description")
    private String description;

    @Column(name="isdeleted")
    private boolean isdeleted;

    //define constructor
    public Project(){}

    public Project(String name, String description,  boolean isdeleted) {
        this.name = name;
        this.description = description;

        this.isdeleted = isdeleted;
    }


}
