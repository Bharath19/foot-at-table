package com.food.table.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.food.table.dto.UserAccount;
import com.food.table.dto.UserDevice;
import com.food.table.exception.ApplicationException;
import com.food.table.model.DeviceRequest;
import com.food.table.model.NotificationModel;
import com.food.table.repo.UserDeviceRepository;
import com.food.table.repo.UserRepository;
import com.food.table.service.NotificationService;
import com.food.table.service.PushNotificationService;
import com.food.table.service.SmsNotificationService;
import com.food.table.util.UserUtil;

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
	
	@Autowired
	private UserDeviceRepository userDeviceRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserUtil userUtil;

	@Override
	public String publish(NotificationModel notificationModel) {
		String messageId = null;
		Optional<UserAccount> userAccount = userRepository.findById(notificationModel.getUserId());
		if(userAccount.isPresent()) {
		if (notificationModel.getNotificationType().equalsIgnoreCase("sms")) {
			messageId = smsNotificationService.smsNotification(notificationModel.getNotificationText(),
					userAccount.get().getPhoneNo().toString());
		} else if (notificationModel.getNotificationType().equalsIgnoreCase("push")) {
			UserDevice userDevice = userDeviceRepository.findByUseraccount(userAccount.get());
			log.info("Push notification started for deviceId : " + notificationModel.getUserId());			
			if (notificationModel.getNotificationType().equalsIgnoreCase("push")) {
				messageId = pushNotificationService.pushNotification(notificationModel.getNotificationText(),
						userDevice.getDeviceToken());
			}
			log.info("Push notification completed for deviceId : " + notificationModel.getUserId());
		} else {
			new ApplicationException(HttpStatus.BAD_REQUEST, "Invalid Notification Type");
		}
		}
		return messageId;
	}

	@Override
	public void addDeviceToken(DeviceRequest deviceRequest) {
		UserAccount currentUser = userUtil.getCurrentUserId();
		UserDevice userDevice = userDeviceRepository.findByUseraccount(currentUser);
		if(userDevice!=null) {
			userDevice.setDeviceType(deviceRequest.getDeviceType());
			userDevice.setDeviceToken(deviceRequest.getDeviceToken());
			userDeviceRepository.save(userDevice);
		} else {
			UserDevice userDeviceNew = new UserDevice();
			userDeviceNew.setUseraccount(currentUser);
			userDeviceNew.setDeviceType(deviceRequest.getDeviceType());
			userDeviceNew.setDeviceToken(deviceRequest.getDeviceToken());
			userDeviceRepository.save(userDeviceNew);
		}

	}

}
