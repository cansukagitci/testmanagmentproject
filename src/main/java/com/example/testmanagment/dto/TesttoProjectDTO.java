package com.example.testmanagment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TesttoProjectDTO {
    private Long projectid;
    private List<Long> testId;

    public TesttoProjectDTO() {
    }

    public TesttoProjectDTO(Long projectid, List<Long> testId) {
        this.projectid = projectid;
        this.testId = testId;
    }
}
