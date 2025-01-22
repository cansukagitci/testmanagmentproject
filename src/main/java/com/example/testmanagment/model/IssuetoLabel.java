package com.example.testmanagment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ref_issue_to_label")
@IdClass(IssuetoLabel.class)
@Getter
@Setter
@ToString
public class IssuetoLabel {
    @Id
    @ManyToOne
    @JoinColumn(name = "issue_id",referencedColumnName = "id",nullable = false)
    private Issues issue;

    @Id
    @ManyToOne
    @JoinColumn(name = "label_id",referencedColumnName = "id",nullable = false)
    private Label label;

    public IssuetoLabel(){}

    public IssuetoLabel(Issues issue, Label label) {
        this.issue = issue;
        this.label = label;
    }
}
