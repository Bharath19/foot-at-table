package com.food.table.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartFoodOptionResponseModel  implements Serializable{
	private String name;
	private String description;
	private String imageUrl;
	private int quantity;
	private double price;
}