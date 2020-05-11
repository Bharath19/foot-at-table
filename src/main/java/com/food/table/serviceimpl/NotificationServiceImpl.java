package com.food.table.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.table.dto.constant.ApplicationConstants;
import com.food.table.exception.ApplicationException;
import com.food.table.model.NotificationModel;
import com.food.table.service.NotificationService;
import com.food.table.service.PushNotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

	@Value("${sns.arn.topicname}")
	private String snsTopicARN;

	@Autowired
	private PushNotificationService pushNotificationService;

	@Override
	public void publish(NotificationModel notificationModel) {
		ObjectMapper mapper = new ObjectMapper();
		String notificationMessage = null;
		try {
			notificationMessage = mapper.writeValueAsString(notificationModel);
		} catch (JsonProcessingException e1) {
			log.error("Exception while getting topic from sns", e1);
			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationConstants.SNS_JSON_PARSE_EXCEPTION);
		}
		MessageAttributeValue attributeValue = new MessageAttributeValue();
		attributeValue.setDataType(ApplicationConstants.MESSAGE_DATA_TYPE);
		attributeValue.setStringValue(notificationModel.getNotificationType());
		Map<String, MessageAttributeValue> messageAttribute = new HashMap<String, MessageAttributeValue>();
		messageAttribute.put(ApplicationConstants.NOTIFICATION_TYPE, attributeValue);
		PublishRequest publishRequest = new PublishRequest(snsTopicARN, notificationMessage);
		publishRequest.withMessageAttributes(messageAttribute);
		pushNotificationService.pushNotification(notificationModel.getNotificationText(),
				notificationModel.getRecipientId());
	}

}
