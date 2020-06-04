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
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.NotFoundException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.food.table.constant.ApplicationConstants;
import com.food.table.dto.UserDevice;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.repo.UserDeviceRepository;
import com.food.table.service.PushNotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PushNotificationServiceImpl implements PushNotificationService {

	@Autowired
	private AmazonSNS amazonSNS;

	@Value("${sns.arn.topicname}")
	private String snsTopicARN;

	@Value("${sns.app.endpointTRN}")
	private String appEndpointTRN;
	
	private UserDeviceRepository userDeviceRepository;

	@Override
	public String pushNotification(String message, String deviceId) {
		String targetArn = registerWithSNS(deviceId);
		try {
			return publish(message, targetArn);
		} catch (Exception e) {
			log.error("Push notification failed for deviceId : " + deviceId);
			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationErrors.PUSH_NOTIFICATION_FAILED);
		}
	}

	public String publish(String subject, String targetArn) throws Exception {
		String messageId = null;
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
		try {
			PublishResult responseSNS = amazonSNS.publish(request);
			messageId = responseSNS.getMessageId();
		} catch (Exception e) {
			log.error("Publish message to SNS is failed", e);
			throw new ApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationErrors.PUBLISH_MESSAGE_FAILED);
		}
		return messageId;
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
						ApplicationErrors.CREATE_ENDPOINT_FAILED);
			}
		}
		storeEndPointArn(deviceToken,endpointArn);
		return endpointArn;
	}

	public String registerWithSNS(String deviceToken) {

		String endpointArn = retrieveEndpointArn(deviceToken);
		boolean updateNeeded = false;
		boolean createNeeded = (null == endpointArn);
		if (createNeeded) {
			endpointArn = createEndpoint(deviceToken);
			createNeeded = false;
		}
		try {
			GetEndpointAttributesRequest geaReq = new GetEndpointAttributesRequest().withEndpointArn(endpointArn);
			GetEndpointAttributesResult geaRes = amazonSNS.getEndpointAttributes(geaReq);

			updateNeeded = !geaRes.getAttributes().get("Token").equals(deviceToken)
					|| !geaRes.getAttributes().get("Enabled").equalsIgnoreCase("true");

		} catch (NotFoundException nfe) {
			log.info("Endpoint not created and retrigger the create end point");
			createNeeded = true;
		}

		if (createNeeded) {
			createEndpoint(deviceToken);
		}
		log.info("EndpointARN updateNeeded : " + updateNeeded);
		if (updateNeeded) {
			System.out.println("Updating platform endpoint " + endpointArn);
			Map<String, String> attribs = new HashMap<String, String>();
			attribs.put("Token", deviceToken);
			attribs.put("Enabled", "true");
			SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest().withEndpointArn(endpointArn)
					.withAttributes(attribs);
			amazonSNS.setEndpointAttributes(saeReq);
		}
		return endpointArn;
	}
	

	private void storeEndPointArn(String deviceToken, String endPointArn) {
		UserDevice userDevice =	userDeviceRepository.findByDeviceToken(deviceToken);
		if(userDevice!=null) {
			userDevice.setEndPointArn(endPointArn);
			userDeviceRepository.save(userDevice);
		}
	}
	
	private String retrieveEndpointArn(String deviceToken) {
		String endPointArn = null;
		UserDevice userDevice =	userDeviceRepository.findByDeviceToken(deviceToken);
		if(userDevice!=null) {
			endPointArn = userDevice.getEndPointArn();
		}
		return endPointArn;
	}

}