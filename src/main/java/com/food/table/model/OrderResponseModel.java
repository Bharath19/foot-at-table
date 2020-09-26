package com.food.table.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.food.table.constant.OrderStateEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderResponseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private RestaurantBasicGetModel restaurant;
	private BasicUserResponseModel userAccount;
	private int restaurantTableId;
	private String orderTypeName;
	@ApiModelProperty(value = "Total order price with GST price")
	private double totalPrice;
	@ApiModelProperty(value = "Total order price with GST price and offer price")
	private double paidPrice;
	private Timestamp orderDate;
	private String offerCode;
	private double offerPrice;
	private double cgst;
	private double sgst;
	private RestaurantEmployeeResponseModel assignee;

	@Enumerated(EnumType.STRING)
	private OrderStateEnum state;

	private List<CartResponseModel> carts;
	
	public double getGst() {
		return getCgst() + getSgst();
	}

}
