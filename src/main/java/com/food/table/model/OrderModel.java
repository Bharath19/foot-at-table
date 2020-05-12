package com.food.table.model;

import java.util.List;

import lombok.Data;

@Data
public class OrderModel {

	private int id;
	private int restaurantId;
	private int userId;
	private int restaurantTableId;
	private int orderType;
	private double totalPrice;
	private double paidPrice;
	private String state;

	private List<CartModel> carts;

}
