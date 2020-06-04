package com.food.table.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ValidateCouponRequest {
	
	
	private int restaurantId;	
	
	private int orderId;
	
	@NotBlank(message = "Coupon code should not be null or empty")
	private String couponCode;
	
	private double billAmount;
}
