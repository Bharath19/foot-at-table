package com.food.table.controller;

import com.food.table.model.NotificationModel;
import com.food.table.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sns/notification")
@Api(value = "Push Notification Management System")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	
	@ApiOperation(value = "Send a push notification to any device")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/publish")
	public void publishNotification(@RequestBody NotificationModel notification) throws Exception {
		notificationService.publish(notification);
	}
}
