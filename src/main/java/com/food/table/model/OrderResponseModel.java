package com.food.table.model;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.food.table.constant.CartStateEnum;
import com.food.table.constant.OrderStateEnum;
import com.food.table.dto.Foods;
import com.food.table.dto.Order;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


import lombok.Data;

@Data
public class OrderResponseModel{

	private int id;
	private RestaurantBasicGetModel restaurant;
	private int userId;
	private int restaurantTableId;
	private int orderType;
	private double totalPrice;
	private double paidPrice;
	private Timestamp orderDate; 
	
	@Enumerated(EnumType.STRING)
	private OrderStateEnum state;
	
	private List<CartResponseModel> carts;

}
