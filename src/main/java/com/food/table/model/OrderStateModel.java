package com.food.table.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.table.constant.OrderStateEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderStateModel implements Serializable{
	
	@Enumerated(EnumType.STRING)
	private OrderStateEnum state;

	private List<CartStateModel> carts;
	
}
