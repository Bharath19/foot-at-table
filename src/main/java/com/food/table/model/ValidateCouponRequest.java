package com.food.table.model;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.table.dto.UserAccount;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateCouponRequest {
	
	private int restaurantId;	
	
	private int orderId;
	
	@NotBlank(message = "Coupon code should not be null or empty")
	private String couponCode;
	
	private double billAmount;
	
	@JsonIgnore
	private UserAccount userAccount;
}
