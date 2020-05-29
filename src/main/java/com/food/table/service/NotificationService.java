package com.food.table.service;

import org.springframework.stereotype.Service;

import com.food.table.model.NotificationModel;

@Service
public interface NotificationService {

	public String publish(NotificationModel notificationModel);
}
