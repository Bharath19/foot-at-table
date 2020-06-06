package com.food.table.model;

import java.sql.Timestamp;
import java.util.List;

import com.food.table.constant.OrderStateEnum;

import io.swagger.annotations.ApiModelProperty;

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
	@ApiModelProperty(value = "Total order price with GST price")
	private double totalPrice;
	@ApiModelProperty(value = "User paied price")
	private double paidPrice;
	@ApiModelProperty(value = "Total order price with GST price and offer price")
	private double paymentPrice;
	private Timestamp orderDate;
	private String offerCode;
	private double offerPrice;
	private double cgst;
	private double sgst;

	@Enumerated(EnumType.STRING)
	private OrderStateEnum state;

	private List<CartResponseModel> carts;
	
	public double getGst() {
		return getCgst() + getSgst();
	}

}
