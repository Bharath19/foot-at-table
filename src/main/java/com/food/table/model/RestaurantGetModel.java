package com.food.table.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RestaurantGetModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private String description;

	private String imageUrl;

	private String status;

	private double rating;

	private AddressModel address;

	private List<String> cuisines;

	private List<String> types;

	private float avgPricePerPerson;

	private List<TimingModel> timingModel;

	private double cgst;

	private double sgst;

}
