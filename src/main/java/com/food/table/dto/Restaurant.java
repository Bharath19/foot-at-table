package com.food.table.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="restaurant_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
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
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private Account account;
	
	private String status;
	
	private String state;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private Tiers tier;
	
	private int avgPricePerPerson;
	
	private double rating;
	
	private int ratingCount;
	
	private String imageUrl;
	
	private String description;
	
	private boolean alcoholServed;
	
	private double cgst;
	
	private double sgst;
	
	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="restaurant_payments",joinColumns = {@JoinColumn(name="restaurant_id", referencedColumnName = "id")}
								,inverseJoinColumns ={@JoinColumn(name="payments_id", referencedColumnName = "id")})
	private List<Payments> payments;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="restaurant_seatings",joinColumns = {@JoinColumn(name="restaurant_id", referencedColumnName = "id")}
								,inverseJoinColumns ={@JoinColumn(name="seatings_id", referencedColumnName = "id")})
	private List<Seatings> seatings;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="restaurant_services",joinColumns = {@JoinColumn(name="restaurant_id", referencedColumnName = "id")}
								,inverseJoinColumns ={@JoinColumn(name="services_id", referencedColumnName = "id")})
	private List<Services> services;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="restaurant_types",joinColumns = {@JoinColumn(name="restaurant_id", referencedColumnName = "id")}
								,inverseJoinColumns ={@JoinColumn(name="types_id", referencedColumnName = "id")})
	private List<Types> types;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="restaurant_cuisines",joinColumns = {@JoinColumn(name="restaurant_id", referencedColumnName = "id")}
								,inverseJoinColumns ={@JoinColumn(name="cuisines_id", referencedColumnName = "id")})
	private List<Cuisines> cuisines;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="restaurant_searchtype",joinColumns = {@JoinColumn(name="restaurant_id", referencedColumnName = "id")}
								,inverseJoinColumns ={@JoinColumn(name="searchtype_id", referencedColumnName = "id")})
	private List<SearchType> searchType;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="restaurant_diet",joinColumns = {@JoinColumn(name="restaurant_id", referencedColumnName = "id")}
								,inverseJoinColumns ={@JoinColumn(name="diet_id", referencedColumnName = "id")})
	private List<Diets> diets;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "restaurant_id", referencedColumnName = "id")
	private List<Timings> timings;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;	
}
