package com.food.table.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.food.table.constant.CartStateEnum;

import lombok.Data;

@Data
public class CartStateModel {

	private int id;

	@Enumerated(EnumType.STRING)
	private CartStateEnum state;

}
