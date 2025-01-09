package com.example.testmanagment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDto {
    private Long id;
    private String pre_condition;
    private String test_input;
    private String assumptions;
    private String expected_test_result;
    private String description;
    private String result;
    private boolean isdeleted;

    public TestDto(){}

    public TestDto(String pre_condition, String test_input, String assumptions, String expected_test_result, String description, String result, boolean isdeleted) {
        this.pre_condition = pre_condition;
        this.test_input = test_input;
        this.assumptions = assumptions;
        this.expected_test_result = expected_test_result;
        this.description = description;
        this.result = result;
        this.isdeleted = isdeleted;
    }
}
