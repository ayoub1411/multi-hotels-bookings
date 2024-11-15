package com.booking.notifications;

public interface NotificationService {

    public void sendMessage(String to, String subject, String body);

}
