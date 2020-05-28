package com.food.table.model;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Positive;

import com.food.table.constant.CartStateEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CartModel {

	@ApiModelProperty(value = "Cart Id is not required for new request. it required for update request")
	private int id;

	@Positive(message = "foodId not found")
	private int foodId;

	@Positive(message = "food quantity should be grater than equal one")
	private int quantity;

	@Enumerated(EnumType.STRING)
	private CartStateEnum state = CartStateEnum.REQUESTED;

	@ApiModelProperty(value = "Total price of each food. ex(food_price * quantity). But it's optional for new request")
	private double price;
	
	@Positive(message = "This optional model required only if user choose the extra(aka customization) options")
	private List<CartFoodOptionModel> cartFoodOptionModel;

}
