package com.example.testmanagment.model;

import jakarta.persistence.*;

@Entity
@Table(name="projectlabels")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="labelname")
    private String labelName;

    @Column(name="labeldescription")
    private String labelDescription;


    @Column(name="labelcolor")
    private String labelColor;

    //const
    public Label(){}

    public Label(String labelName, String labelDescription, String labelColor) {
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelColor = labelColor;
    }


    //getter and setter


    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelDescription() {
        return labelDescription;
    }

    public void setLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    //toString

    @Override
    public String toString() {
        return "Label{" +
                "labelName='" + labelName + '\'' +
                ", labelDescription='" + labelDescription + '\'' +
                ", labelColor='" + labelColor + '\'' +
                '}';
    }
}
