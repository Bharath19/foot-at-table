package com.food.table.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartFoodOptionModel {
	
	@ApiModelProperty(value = "Cart Id is not required for new request. it required for update request")
	private int id;
	
	private int foodOptionId;
	
	private int quantity;
	
}
