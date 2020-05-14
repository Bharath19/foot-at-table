package com.food.table.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.food.table.constant.CartStateEnum;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "carts")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="food_id",referencedColumnName = "id")
	private Foods food;
		
	@Min(value = 1)
	private int quantity;
	
	@ManyToOne
	private Restaurant restaurant;
	
	@Column(length = 8)
	private int state;
	
	private double price;
	
    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * check whether can calculate the order price
     * @return boolean
     */
	public boolean canCalculateTotalPrice() {
		return this.getState() != CartStateEnum.CANCELLED.getId();  
	}
  
}
