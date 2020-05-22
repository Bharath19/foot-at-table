package com.food.table.model;

import java.util.List;

import lombok.Data;

@Data
public class PromotionFoodModel {
	
	private Integer restaurantId;
	
	private List<PromotionFoodChild> foods;

}
