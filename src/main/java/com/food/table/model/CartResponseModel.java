package com.food.table.model;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.food.table.constant.CartStateEnum;

import lombok.Data;


@Data
public class CartResponseModel{

	private int id;
	private FoodResponseModel food;
	private int quantity;
	
	@Enumerated(EnumType.STRING)
	private CartStateEnum state;
	
	private double price;
	
	private List<CartFoodOptionResponseModel> cartFoodOptionResponseModels;

}
