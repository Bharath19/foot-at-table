package com.food.table.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.filefilter.NotFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.table.constant.ApplicationConstants;
import com.food.table.exception.ApplicationException;
import com.food.table.model.NotificationModel;
import com.food.table.service.NotificationService;
import com.food.table.service.PushNotificationService;
import com.food.table.service.SmsNotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

	@Value("${sns.arn.topicname}")
	private String snsTopicARN;

	@Autowired
	private PushNotificationService pushNotificationService;

	@Autowired
	private SmsNotificationService smsNotificationService;

	@Override
	public String publish(NotificationModel notificationModel) {
		String messageId = null;
		if (notificationModel.getNotificationType().equalsIgnoreCase("sms")) {
			messageId = smsNotificationService.smsNotification(notificationModel.getNotificationText(),
					notificationModel.getRecipientId());
		} else if (notificationModel.getNotificationType().equalsIgnoreCase("push")) {
			log.info("Push notification started for deviceId : " + notificationModel.getRecipientId());			
			if (notificationModel.getNotificationType().equalsIgnoreCase("push")) {
				messageId = pushNotificationService.pushNotification(notificationModel.getNotificationText(),
						notificationModel.getRecipientId());
			}
			log.info("Push notification completed for deviceId : " + notificationModel.getRecipientId());
		} else {
			new ApplicationException(HttpStatus.BAD_REQUEST, "Invalid Notification Type");
		}
		return messageId;
	}

}
