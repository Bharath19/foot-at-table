package com.food.table.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.DeviceRequest;
import com.food.table.model.NotificationModel;
import com.food.table.service.NotificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/sns/notification")
@Api(value = "Push Notification Management System")
@Slf4j
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@ApiOperation(value = "Send a push notification to any device", authorizations = {@Authorization(value = "accessToken")})
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/publish")
	public ResponseEntity<String> publishNotification(@RequestBody NotificationModel notification) throws Exception {
		long startTime=System.currentTimeMillis();
		log.info("Entering publish Notification starttime : "+startTime + " and type : "+ notification.getNotificationType());
		String messageId = notificationService.publish(notification);
		long endTime=System.currentTimeMillis();
		log.info("Exiting publish Notification is success and timetaken : "+(endTime-startTime)+ " and type : "+ notification.getNotificationType());
		return ResponseEntity.ok(messageId);
	}
	
	@ApiOperation(value = "Add a device token for user", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/addDeviceToken")
	public ResponseEntity<Void> addUserDeviceToken(@Valid @RequestBody DeviceRequest deviceRequest) {
		long startTime=System.currentTimeMillis();
		log.info("Entering add device token starttime : "+startTime );
		notificationService.addDeviceToken(deviceRequest);
		long endTime=System.currentTimeMillis();
		log.info("Exiting add device token is success and timetaken : "+(endTime-startTime));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
