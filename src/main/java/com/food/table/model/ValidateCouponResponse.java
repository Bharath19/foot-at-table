package com.food.table.model;

import lombok.Data;

@Data
public class ValidateCouponResponse {
	
	private double offerAmount;
	
	private double billAmount;
	
	private String offerCode;
	
}
