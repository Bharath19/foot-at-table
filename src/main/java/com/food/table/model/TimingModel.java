package com.food.table.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimingModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String day;
	
	private String from;
	
	private String to;
}
