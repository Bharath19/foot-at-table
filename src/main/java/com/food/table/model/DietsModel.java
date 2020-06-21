package com.food.table.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DietsModel  implements Serializable {	
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
	
	private String description;
}
