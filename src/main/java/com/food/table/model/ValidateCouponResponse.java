package com.food.table.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.table.dto.Offers;

import lombok.Data;

@Data
public class ValidateCouponResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private double offerAmount;
	
	private double billAmount;
	
	private String offerCode;
	
	@JsonIgnore
	private Offers offer;
	
}
