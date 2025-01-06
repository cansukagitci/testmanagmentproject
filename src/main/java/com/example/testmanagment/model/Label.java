package com.example.testmanagment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="projectlabels")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="labelname")
    private String labelName;

    @Column(name="labeldescription")
    private String labelDescription;


    @Column(name="labelcolor")
    private String labelColor;

    @Column(name="isdeleted")
    private Boolean isdeleted;

    //const
    public Label(){}

    public Label(String labelName, String labelDescription, String labelColor, Boolean isdeleted) {
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelColor = labelColor;
        this.isdeleted = isdeleted;
    }

    //getter and setter


    //toString



}
