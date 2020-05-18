package com.food.table.model;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Positive;

import com.food.table.constant.OrderStateEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderModel {

	@Positive(message = "restaurantId not found")
	private int restaurantId;
	
	@Positive(message = "userId not found")
	private int userId;
	
	@Positive(message = "restaurantTableId not found")
	private int restaurantTableId;
	
	@Positive(message = "orderType not found")
	private int orderType;
	
	@ApiModelProperty(value = "Total price of the order. Optional field for new request")
	private double totalPrice;
	
	@ApiModelProperty(value = "Optional field for new request")
	private double paidPrice;

	@Enumerated(EnumType.STRING)
	private OrderStateEnum state;

	private List<CartModel> carts;

}
