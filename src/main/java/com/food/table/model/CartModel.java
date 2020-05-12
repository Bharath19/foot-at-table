package com.food.table.model;

import lombok.Data;

@Data
public class CartModel {
	
  private int id;
  private int foodId;
  private int quantity;
  private String state;
  private double price;
  
}
