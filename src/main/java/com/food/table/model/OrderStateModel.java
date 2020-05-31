package com.food.table.model;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.table.constant.OrderStateEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderStateModel {
	
	@Enumerated(EnumType.STRING)
	private OrderStateEnum state;

	private List<CartStateModel> carts;
	
	@ApiModelProperty(value = "Optional param. It required if user have offer code on BILL_REQUESTED state")
	private String offerCode;
	
	@JsonIgnore
	private double offerAmount;
}
