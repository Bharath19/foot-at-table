package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoodResponseModel {
	private int id;
	private String name;
	private String description;
	private String imageUrl;
	private double price;
}
