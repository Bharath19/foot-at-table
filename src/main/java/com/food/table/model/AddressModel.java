package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressModel {
	
	private String line1;
	
	private String line2;
	
	private String district;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String pincode;
	
	private String lattitude;
	
	private String longitude;

}
