package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartFoodOptionResponseModel {
	private String name;
	private String description;
	private String imageUrl;
	private int quantity;
	private double price;
}