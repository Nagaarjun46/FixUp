package com.example.fixup.apicalls.models;

import com.example.fixup.models.NotificationModel;

import java.util.List;

public class NotificationResponse {
    public boolean success;
    public List<NotificationModel> notifications;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<NotificationModel> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationModel> notifications) {
        this.notifications = notifications;
    }
}


