package com.food.table.model;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.table.constant.ApplicationConstants;
import com.food.table.constant.OrderStateEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OrderModel {

	@Positive(message = "restaurantId not found")
	private int restaurantId;
	
	@ApiModelProperty(value = "If user details is not avaiable. Order will be create on logged in user")
	@JsonIgnore
	private int userId;
	
	@ApiModelProperty(value = "If order belongs to dineIn/self-service type, restaurantTableId is required. Otherwise, not required")
	private int restaurantTableId;
	
	@Positive(message = "orderType not found")
	private String orderType;
	
	@Enumerated(EnumType.STRING)
	@JsonIgnore
	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private OrderStateEnum state;

	private List<CartModel> carts;

	public OrderStateEnum getState() {
		if(isDineInType()) {
			return OrderStateEnum.REQUESTED;
		}	
		return OrderStateEnum.DRAFTED;
	}

	@JsonIgnore
	public boolean isDineInType() {
		return getOrderType() != null && getOrderType().equalsIgnoreCase(ApplicationConstants.dineInTypeText);	 
	}
	
	@JsonIgnore
	public boolean isSelfServiceType() {
		return getOrderType() != null && getOrderType().equalsIgnoreCase(ApplicationConstants.selfServiceTypeText);	 
	}
	
}
