package com.food.table.model;

import java.util.Date;

import lombok.Data;

@Data
public class UserOfferMonitorResponse {
	
	private String restaurantName;
	
	private double offerAmount;
	
	private Date createdAt;
	
}
