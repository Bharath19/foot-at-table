package com.food.table.model;


import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackModel {	
	
	private int orderId;
	
	private String message;
	
	private double rating;
}
