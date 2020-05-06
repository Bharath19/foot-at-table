package com.food.table.model;

import java.util.List;

import lombok.Data;

@Data
public class SearchModel {
	
	private List<String> restaurantService;
	
	private List<String> restaurantSeating;
	
	private List<String> restaurantCuisine;
	
	private List<String> restaurantType;
	
	private List<String> restaurantDiet;

}
