package com.food.table.service;

import org.springframework.stereotype.Service;

@Service
public interface PushNotificationService {

	public void pushNotification(String message, String deviceId);
}
