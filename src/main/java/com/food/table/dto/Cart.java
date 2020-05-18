package com.food.table.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.food.table.constant.CartOrderStatus;
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
	
	@Enumerated(EnumType.STRING)
	private CartStateEnum state;
	
	@PositiveOrZero
	private double price;
	
    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_status", length = 25)
    @Enumerated(EnumType.STRING)
    private CartOrderStatus  OrderStatus = CartOrderStatus.WORK_IN_PROCESS;
}
