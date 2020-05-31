package com.food.table.model;

import java.util.List;

import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class CartModel {

	@Positive(message = "foodId not found")
	private int foodId;

	@Positive(message = "food quantity should be grater than equal one")
	private int quantity;
	
	@Positive(message = "This optional model required only if user choose the extra(aka customization) options")
	private List<CartFoodOptionModel> cartFoodOptionModel;

}
