package com.food.table.model;

import java.util.Date;

import lombok.Data;

@Data
public class UserOfferModel {
	
	private int userId;	
	
	private int usageCount;
	
	private Date expirationDate;
}
