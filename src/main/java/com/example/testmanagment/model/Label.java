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
    private String labelname;

    @Column(name="labeldescription")
    private String labeldescription;


    @Column(name="labelcolor")
    private String labelcolor;

    @Column(name="isdeleted")
    private Boolean isdeleted;

    //const
    public Label(){}

    public Label(String labelname, String labeldescription, String labelcolor, Boolean isdeleted) {
        this.labelname = labelname;
        this.labeldescription = labeldescription;
        this.labelcolor = labelcolor;
        this.isdeleted = isdeleted;
    }

    //getter and setter


    //toString



}
