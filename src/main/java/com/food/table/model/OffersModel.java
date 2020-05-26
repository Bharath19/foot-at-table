package com.food.table.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OffersModel {
	
	private String offerCode;
	
	private double offerAmount;
	
	private boolean percentage;
	
	private double minBillAmount;
	
	private double maxOfferAmount;
	
	private Date expirationDate;
	
	private String offerType;
	
	private String state;
	
	private Integer usageCount;
	
	private Integer usageType;
	
	private boolean allRestaurant;
	
	private boolean allUsers;
	
	private List<RestaurantOfferModel> restaurants;	
	
	private List<UserOfferModel> users;
}
