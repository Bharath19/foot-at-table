package com.food.table.dto;

import java.util.Date;
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


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "food_categories")
public class FoodCategory {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String description;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;	
}