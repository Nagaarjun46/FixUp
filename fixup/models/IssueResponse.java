package com.example.fixup.models;

import java.util.List;

public class IssueResponse {
    private boolean success;
    private List<Issue> issues;

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }
}

