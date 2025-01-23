package com.example.testmanagment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ref_issue_to_user")
@IdClass(IssuetoUser.class) // `IssuetoUserId` bir id class olmalı
@Getter
@Setter
@ToString
public class IssuetoUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "issue_id", referencedColumnName = "id", nullable = false)
    private Issues issue;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public IssuetoUser(){}

    public IssuetoUser(Issues issue, User user) {
        this.issue = issue;
        this.user = user;
    }
}
