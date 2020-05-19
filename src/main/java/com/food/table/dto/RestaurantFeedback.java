package com.food.table.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="restaurant_feedback")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantFeedback {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="restaurant_id",referencedColumnName = "id")
	private Restaurant restaurant;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id",referencedColumnName = "id")
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",referencedColumnName = "id")
	private UserAccount user;
	
	private String message;
	
	private double rating;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;	

}
