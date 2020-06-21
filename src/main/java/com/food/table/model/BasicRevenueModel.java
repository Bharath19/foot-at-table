package com.food.table.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicRevenueModel  implements Serializable{

	private static final long serialVersionUID = 1L;

	List<RevenueDetailsModel> revenueDetails;

	double totalPrice;
	
	public BasicRevenueModel(List<RevenueDetailsModel> revenueDetails, double totalPrice){
		this.revenueDetails = revenueDetails;
		this.totalPrice = totalPrice;
 	}

}
