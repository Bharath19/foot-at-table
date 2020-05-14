package com.food.table.serviceimpl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.food.table.constant.ApplicationConstants;
import com.food.table.exception.ApplicationException;
import com.food.table.service.PushNotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PushNotificationServiceImpl implements PushNotificationService {

	@Autowired
	private AmazonSNS amazonSNS;

	@Value("${sns.app.endpointTRN}")
	private String appEndpointTRN;

	@Override
	public void pushNotification(String message, String deviceId) {
		log.info("Push notification started for deviceId : " + deviceId);
		String targetArn = createEndpoint(deviceId);
		try {
			publish(message, targetArn);
		} catch (Exception e) {
			log.error("Push notification failed for deviceId : " + deviceId);
			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationConstants.PUSH_NOTIFICATION_FAILED);
		}
		log.info("Push notification completed for deviceId : " + deviceId);
	}

	public void publish(String subject, String targetArn) throws Exception {
		PublishRequest request = new PublishRequest();
		request.setMessageStructure(ApplicationConstants.GCM_MSG_STRUCTURE);
		Map<String, Map<String, String>> androidMsg = new HashMap<>();
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put(ApplicationConstants.TEXT, subject);
		androidMsg.put(ApplicationConstants.NOTIFICATION, messageMap);
		JSONObject json = new JSONObject(androidMsg);
		String message = json.toString();
		Map<String, String> msgMap = new HashMap<String, String>();
		msgMap.put(ApplicationConstants.GCM, message);
		msgMap.put("default", "default message");
		JSONObject msgMapObject = new JSONObject(msgMap);
		String sendMsg = msgMapObject.toString();
		request.setTargetArn(targetArn);
		request.setMessage(sendMsg);
	}

	private String createEndpoint(String deviceToken) {
		String endpointArn = null;
		try {
			CreatePlatformEndpointRequest cpeReq = new CreatePlatformEndpointRequest()
					.withPlatformApplicationArn(appEndpointTRN).withToken(deviceToken);
			CreatePlatformEndpointResult cpeRes = amazonSNS.createPlatformEndpoint(cpeReq);
			endpointArn = cpeRes.getEndpointArn();
		} catch (InvalidParameterException ipe) {
			String message = ipe.getErrorMessage();
			Pattern p = Pattern.compile(".*Endpoint (arn:aws:sns[^ ]+) already exists " + "with the same [Tt]oken.*");
			Matcher m = p.matcher(message);
			if (m.matches()) {
				endpointArn = m.group(1);
			} else {
				throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
						ApplicationConstants.CREATE_ENDPOINT_FAILED);
			}
		}
		return endpointArn;
	}
}
