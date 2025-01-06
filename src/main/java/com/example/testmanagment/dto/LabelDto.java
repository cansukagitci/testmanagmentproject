package com.example.testmanagment.dto;

public class LabelDto {

    private String labelname;
    private String labeldescription;
    private String labelcolor;
    private boolean isdeleted;


    //const
    public LabelDto(){}

    public LabelDto(String labelname, String labeldescription, String labelcolor, boolean isdeleted) {
        this.labelname = labelname;
        this.labeldescription = labeldescription;
        this.labelcolor = labelcolor;
        this.isdeleted = isdeleted;
    }

    //getter and setter


    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname;
    }

    public String getLabeldescription() {
        return labeldescription;
    }

    public void setLabeldescription(String labeldescription) {
        this.labeldescription = labeldescription;
    }

    public String getLabelcolor() {
        return labelcolor;
    }

    public void setLabelcolor(String labelcolor) {
        this.labelcolor = labelcolor;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }
}
