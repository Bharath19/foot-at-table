package com.food.table.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.food.table.dto.Order;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.model.OrderStateModel;

@Service
public interface OrderService {

	Order createOrder(OrderModel orderModel);
	
	OrderResponseModel getOrderById(int orderid);

	Order updateOrder(OrderModel orderModel, int orderId);

	Order updateOrderState(OrderStateModel orderModel, int orderId);

	ArrayList<OrderResponseModel> getOrderByUserId(int userId);

}
