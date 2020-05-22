package com.food.table.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PromotionRestaurantModel {
	
	@NotNull(message = "Restaurant Id must not be  null")
	private Integer restaurantId;
	
	@NotNull(message = "Rank must not be null")
	private Integer rank;
}
