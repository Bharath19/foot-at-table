package com.food.table.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicUserResponseModel {
	
	private int id;
	private String name;
	private String imageUrl;
	private long phoneNo;

}
