package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimingModel {

	private String day;
	
	private String from;
	
	private String to;
}
