package com.example.fixup.models;

import java.util.ArrayList;
import java.util.List;

public class IssueDetailModel {
    public String issueId;
    public String userEmail;
    public String imageUri;
    public String issueTitle;
    public String issueContent;
    public String issueCity;
    public String issueArea;
    public int issuePriority;
    public int issueVolunteersNeeded;
    public String issueCreatedAt;
    public List<String> issueVolunteers;
    public boolean isIssueOpen;

    public IssueDetailModel(String userEmail, String imageUri, String issueTitle, String issueContent,
                            String issueCity, String issueArea, int issuePriority, int issueVolunteersNeeded) {
        this.userEmail = userEmail;
        this.imageUri = imageUri;
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueCity = issueCity;
        this.issueArea = issueArea;
        this.issuePriority = issuePriority;
        this.issueVolunteersNeeded = issueVolunteersNeeded;
        this.issueVolunteers = new ArrayList<>();
        this.isIssueOpen = true;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public String getIssueCity() {
        return issueCity;
    }

    public String getIssueArea() {
        return issueArea;
    }

    public int getIssuePriority() {
        return issuePriority;
    }

    public int getIssueVolunteersNeeded() {
        return issueVolunteersNeeded;
    }

    public List<String> getIssueVolunteers() {
        return issueVolunteers;
    }

    public boolean isIssueOpen() {
        return isIssueOpen;
    }
    public void setIsIssueOpen() {
        this.isIssueOpen = false;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public void setIssueCity(String issueCity) {
        this.issueCity = issueCity;
    }

    public void setIssueArea(String issueArea) {
        this.issueArea = issueArea;
    }

    public void setIssuePriority(int issuePriority) {
        this.issuePriority = issuePriority;
    }

    public void setIssueVolunteersNeeded(int issueVolunteersNeeded) {
        this.issueVolunteersNeeded = issueVolunteersNeeded;
    }

    public void setIssueVolunteers(List<String> issueVolunteers) {
        this.issueVolunteers = issueVolunteers;
    }

    public void setIssueOpen(boolean issueOpen) {
        isIssueOpen = issueOpen;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getIssueCreatedAt() {
        return issueCreatedAt;
    }

    public void setIssueCreatedAt(String issueCreatedAt) {
        this.issueCreatedAt = issueCreatedAt;
    }
}