CREATE TABLE ref_issue_to_user (
    issue_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (issue_id, user_id),
    FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);