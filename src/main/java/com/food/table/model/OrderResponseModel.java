package com.food.table.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.food.table.constant.CartStateEnum;
import com.food.table.constant.OrderStateEnum;
import com.food.table.dto.Foods;
import com.food.table.dto.Order;

import lombok.Data;

@Data
public class OrderResponseModel{

	private int id;
	private int restaurantId;
	private int userId;
	private int restaurantTableId;
	private int orderType;
	private double totalPrice;
	private double paidPrice;
	private String state;
	
	private List<CartResponseModel> carts;

	public static OrderResponseModel convertToOrderResponse(Order order) {
		if(order == null)
			return null;
		
		OrderResponseModel orderResponseModel = new OrderResponseModel();
		orderResponseModel.setId(order.getId());
		orderResponseModel.setRestaurantId(order.getRestaurant().getId());
		orderResponseModel.setUserId(order.getUserId());
		orderResponseModel.setRestaurantTableId(order.getRestaurantTable().getId());
		orderResponseModel.setOrderType(order.getType().getId());
		orderResponseModel.setTotalPrice(order.getTotalPrice());
		orderResponseModel.setPaidPrice(order.getPaidPrice());
		orderResponseModel.setState(OrderStateEnum.getValue(order.getState()));
		
		List<CartResponseModel> cartResponseList = new ArrayList<CartResponseModel>();
		order.getCarts().forEach(cart -> {
			CartResponseModel cartResponseModel = new CartResponseModel();
			Foods food=cart.getFood();
			FoodResponseModel foodResponseModel = FoodResponseModel.builder().id(food.getId()).name(food.getName())
					.description(food.getDescription()).imageUrl(food.getImageUrl()).price(food.getPrice()).build();
			cartResponseModel.setFood(foodResponseModel);
			cartResponseModel.setId(cart.getId());
			cartResponseModel.setPrice(cart.getPrice());
			cartResponseModel.setQuantity(cart.getQuantity());
			cartResponseModel.setState(CartStateEnum.getValue(cart.getState()));
			cartResponseList.add(cartResponseModel);
		});
		
		orderResponseModel.setCarts(cartResponseList);
		return orderResponseModel;
	}

}
