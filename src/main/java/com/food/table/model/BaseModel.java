package com.food.table.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
}
