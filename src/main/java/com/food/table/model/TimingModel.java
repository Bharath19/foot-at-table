package com.food.table.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TimingModel {

	private String day;
	
	private String from;
	
	private String to;
}
