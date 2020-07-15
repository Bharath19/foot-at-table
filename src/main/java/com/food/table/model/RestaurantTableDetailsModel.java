package com.food.table.model;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantTableDetailsModel  implements Serializable {

	private static final long serialVersionUID = 1L;

	int restaurantId;

    int restuarantTableId;

    String restaurantName;

    List<String> restaurantType;

	String imageUrl;



}