package com.food.table.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="address_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;	
	
	private String line1;
	
	private String line2;
	
	private String district;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String pincode;	
	
	@Column(precision = 8,scale = 6)
	private double lattitude;
	
	@Column(precision = 9,scale = 6)
	private double longitude;
	
	@OneToOne(mappedBy = "address")
	private Restaurant restaurant;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;	
}
