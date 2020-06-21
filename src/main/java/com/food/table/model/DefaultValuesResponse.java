package com.food.table.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DefaultValuesResponse  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<BaseModel> payments;
	
	private List<BaseModel> seatings;
	
	private List<BaseModel> services;
	
	private List<BaseModel> types;
	
	private List<BaseModel> diets;
	
	private List<BaseModel> cuisines;
	
	private List<BaseModel> searchTags;
}
