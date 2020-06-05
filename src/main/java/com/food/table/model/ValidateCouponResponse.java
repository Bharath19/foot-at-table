package com.food.table.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.table.dto.Offers;

import lombok.Data;

@Data
public class ValidateCouponResponse {
	
	private double offerAmount;
	
	private double billAmount;
	
	private String offerCode;
	
	@JsonIgnore
	private Offers offer;
	
}
