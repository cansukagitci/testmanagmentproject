package com.example.testmanagment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelDto {
    private Long id;
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


}
