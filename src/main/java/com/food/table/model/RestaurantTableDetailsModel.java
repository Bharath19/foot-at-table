package com.food.table.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantTableDetailsModel  implements Serializable {

	private static final long serialVersionUID = 1L;

	int restaurantId;

    int tableId;
}