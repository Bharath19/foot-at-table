package com.food.table.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.food.table.constant.OrderStateEnum;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", referencedColumnName = "id")
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserAccount userAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_table_id", referencedColumnName = "id")
	private RestaurantTable restaurantTable;

	@ManyToOne(fetch = FetchType.LAZY)
	private Types type;

	@Column(name = "total_price", nullable = false)
	private double totalPrice;

	@Column(name = "paid_price")
	private double paidPrice;

	@Enumerated(EnumType.STRING)
	private OrderStateEnum state = OrderStateEnum.REQUESTED;

	@CreationTimestamp
	@Column(name = "created_at")
	public Timestamp createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	public Timestamp updatedAt;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private List<Cart> carts = new ArrayList<Cart>();

}
