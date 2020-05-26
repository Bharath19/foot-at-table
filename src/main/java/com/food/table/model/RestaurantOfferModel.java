package com.food.table.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class RestaurantOfferModel {
	
	private int restaurantId;
	
	private boolean allUsers;
	
	private List<UserOfferModel> users;
	
	private Date expirationDate;
}
