package com.food.table.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.food.table.constant.ApplicationConstants;
import com.food.table.exception.ApplicationException;
import com.food.table.service.SmsNotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SmsNotificationServiceImpl implements SmsNotificationService {

	@Autowired
	private AmazonSNS amazonSNS;

	@Override
	public String smsNotification(String message, String deviceId) {
		String messageId = null;
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		smsAttributes.put("AWS.SNS.SMS.SenderID",
				new MessageAttributeValue().withStringValue("CBroot").withDataType("String"));
		smsAttributes.put("AWS.SNS.SMS.SMSType",
				new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));
		try {
			PublishResult result = amazonSNS.publish(new PublishRequest().withPhoneNumber("+91"+deviceId).withMessage(message)
					.withMessageAttributes(smsAttributes));
			messageId = result.getMessageId();
			log.info("sms notification is success for deviceID : " + result.getMessageId());
		} catch (Exception e) {
			log.error("sms notification failed for deviceId : " + deviceId);
			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationConstants.SMS_NOTIFICATION_FAILED);
		}
		return messageId;
	}

}
