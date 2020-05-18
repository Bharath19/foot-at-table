package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantBasicGetModel {
	private int id;

	private String name;

	private String imageUrl;

	private AddressModel address;

}
