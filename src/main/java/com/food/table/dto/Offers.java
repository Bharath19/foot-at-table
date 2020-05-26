package com.food.table.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="offers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offers {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	private String offerCode;
	
	private double offerAmount;
	
	private boolean percentage;
	
	private double minBillAmount;
	
	private double maxOfferAmount;
	
	private Date expirationDate;
	
	private String offerType;
	
	private String state;
	
	private int usageCount;
	
	private int usageType;
	
	private boolean allRestaurant;
	
	private boolean allUsers;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;	
}
