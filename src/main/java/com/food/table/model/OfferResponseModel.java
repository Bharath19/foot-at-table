package com.food.table.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferResponseModel  implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	private String offerCode;
	
	private double offerPercentage;	
	
	private double minBillAmount;
	
	private double maxOfferAmount;
	
	private Date expirationDate;

}
