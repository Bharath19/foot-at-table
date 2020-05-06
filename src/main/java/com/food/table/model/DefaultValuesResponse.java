package com.food.table.model;

import java.util.List;

import lombok.Data;

@Data
public class DefaultValuesResponse {
	
	private List<BaseModel> payments;
	
	private List<BaseModel> seatings;
	
	private List<BaseModel> services;
	
	private List<BaseModel> types;
}
