package com.food.table.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserOfferMonitorResponse  implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	private String restaurantName;
	
	private double offerAmount;
	
	private Date createdAt;
	
}
