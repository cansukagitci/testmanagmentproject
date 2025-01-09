package com.example.testmanagment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="tests")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="pre_condition")
    private String pre_condition;

    @Column(name="test_input")
    private String test_input;

    @Column(name="assumptions")
    private String assumptions;

    @Column(name="test_item")
    private String test_item;

    @Column(name="expected_test_result")
    private String expected_test_result;

    @Column(name="description")
    private String description;

    @Column(name="result")
    private String result;

    @Column(name="isdeleted")
    private boolean isdeleted;

    public Test(){}

    public Test(String pre_condition, String test_input, String assumptions, String test_item, String expected_test_result, String description, String result, boolean isdeleted) {
        this.pre_condition = pre_condition;
        this.test_input = test_input;
        this.assumptions = assumptions;
        this.test_item = test_item;
        this.expected_test_result = expected_test_result;
        this.description = description;
        this.result = result;
        this.isdeleted = isdeleted;
    }
}
