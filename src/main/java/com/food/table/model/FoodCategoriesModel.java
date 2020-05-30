package com.food.table.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodCategoriesModel {
	
	private int id;
	
	private String name;
	
	private String description;

    private int restaurantId;

    private int sortOrder;
	
}
