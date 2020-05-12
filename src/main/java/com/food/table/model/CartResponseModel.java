package com.food.table.model;

import lombok.Data;


@Data
public class CartResponseModel{

	private int id;
	private FoodResponseModel food;
	private int quantity;
	private String state;
	private double price;

}
