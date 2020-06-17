package com.food.table.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class RestaurantModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String restaurantName;

	private String restaurantPhoneNumber;

	private String restaurantEmailId;

	private String fssaiLicenseNo;

	private String website;

	private String submittedBy;

	private String phoneNumber;

	private String emailId;

	private Boolean restaurantIsOpened;

	private Date restaurantOpenDate;

	private int accountId;

	private String status;

	private String state;

	private int tierId;

	private int avgPricePerPerson;

	private String description;

	private Boolean alcoholServed;

	private AddressModel address;

	private List<Integer> type;

	private List<Integer> services;

	private List<Integer> seating;

	private List<Integer> payment;

	private List<Integer> cuisines;

	private List<Integer> searchTags;

	private List<Integer> dietId;

	private List<TimingModel> timings;

	private String password;

	private String userType;

	private double cgst = 9;

	private double sgst = 9;
}
