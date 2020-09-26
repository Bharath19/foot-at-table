package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationModel {

	private String notificationText;

	private String notificationType;

	private Integer userId;
	
	private long phoneNo;

}
