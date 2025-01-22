package com.example.testmanagment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class IssuetoLabelDTO {
    private Long issue_Id;

    private List<Long> labelId;

    public IssuetoLabelDTO(){}

    public IssuetoLabelDTO(Long issue_Id, List<Long> labelId) {
        this.issue_Id = issue_Id;
        this.labelId = labelId;
    }
}
