package com.food.table.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class FoodsRestaurantModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private int foodCategoryId;

    private String foodCategoryName;

    List<FoodsModel> foods;
}
