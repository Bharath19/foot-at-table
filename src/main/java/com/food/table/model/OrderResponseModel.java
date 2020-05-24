package com.food.table.model;

import java.sql.Timestamp;
import java.util.List;

import com.food.table.constant.OrderStateEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;

@Data
public class OrderResponseModel {

	private int id;
	private RestaurantBasicGetModel restaurant;
	private BasicUserResponseModel userAccount;
	private int restaurantTableId;
	private String orderTypeName;
	private double totalPrice;
	private double paidPrice;
	private Timestamp orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStateEnum state;

	private List<CartResponseModel> carts;

}
