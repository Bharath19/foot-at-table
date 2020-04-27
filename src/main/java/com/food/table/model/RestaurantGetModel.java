package com.food.table.model;

import java.util.List;

import lombok.Data;

@Data
public class RestaurantGetModel {
	
	private int id;
	
	private String name;
	
	private String description;
	
	private String imageUrl;
	
	private AddressModel address;
	
	private List<String> cuisines;
	
	private List<String> types;
	
	private float avgPricePerPerson;
	
	private TimingModel timingModel;

}
 