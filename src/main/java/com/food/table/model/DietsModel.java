package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DietsModel {	
	
	private int id;
	
	private String name;
	
	private String description;
}
