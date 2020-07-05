package com.food.table.model;


import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class FeedbackModel {	
	
	@NotBlank
	private int orderId;
	
	private String message;
	
	private double rating;
}
