package com.food.table.service;

import java.util.ArrayList;

import com.food.table.dto.Order;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;

public interface OrderService {

	Order createOrder(OrderModel orderModel);
	
	OrderResponseModel getOrderById(int orderid);

	Order updateOrder(OrderModel orderModel, int orderId);

	Order updateOrderState(OrderModel orderModel, int orderId);

	ArrayList<OrderResponseModel> getOrderByUserId(int userId);

}
