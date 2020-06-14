package com.food.table.dto;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion_food")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionFood {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id" ,referencedColumnName = "id")
	private Restaurant restaurant;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_id" ,referencedColumnName = "id")
	private Foods foods;
	
	@Min(1)
	@Max(5)
	private Integer rankId;	
	
}
