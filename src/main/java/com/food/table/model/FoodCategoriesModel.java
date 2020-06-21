package com.food.table.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodCategoriesModel  implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
	
	private String description;

    private int restaurantId;

    private int sortOrder;
	
}
