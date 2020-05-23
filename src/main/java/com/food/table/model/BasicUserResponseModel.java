package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicUserResponseModel {
	
	private int id;
	private String name;

}
