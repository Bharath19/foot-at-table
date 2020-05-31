package com.food.table.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel {
	
	private String line1;
	
	private String line2;
	
	private String district;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String pincode;
	
	private Double distance;
	
	private double lattitude;
	
	private double longitude;

}
