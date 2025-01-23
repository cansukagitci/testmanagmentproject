package com.example.testmanagment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class IssuetoUserDTO {
    private Long issue_Id;

    private List<Long> userId;

    public IssuetoUserDTO(){}

    public IssuetoUserDTO(Long issue_Id, List<Long> userId) {
        this.issue_Id = issue_Id;
        this.userId = userId;
    }
}
