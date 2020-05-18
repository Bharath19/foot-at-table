package com.food.table.model;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.food.table.constant.OrderStateEnum;

import lombok.Data;

@Data
public class OrderStateModel {
	
	@Enumerated(EnumType.STRING)
	private OrderStateEnum state;
	
	private List<CartStateModel> carts;
}
