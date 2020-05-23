package com.food.table.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.food.table.dto.Order;
import com.food.table.model.BasicRevenueModel;
import com.food.table.model.CartModel;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.model.OrderStateModel;

@Service
public interface OrderService {

	Order createOrder(OrderModel orderModel);

	OrderResponseModel getOrderById(int orderid);

	Order updateOrder(OrderModel orderModel, int orderId);

	Order updateOrderState(OrderStateModel orderModel, int orderId);

	List<OrderResponseModel> getOrderByUserId(int userId, String orderState, Date orderDate, int from, int limit);

	BasicRevenueModel getBasicRevenue(int restaurantId, Date orderDate);

	List<OrderResponseModel> getOrderByOrderTypeName(int restaurantId, List<String> orderTypes, String orderState,
			Date orderDate, int from, int limit);

	List<OrderResponseModel> getOrderByRestaurantTableId(int restaurantId, int restaurantTableId, String orderState,
			Date orderDate, int from, int limit);

	List<OrderResponseModel> getOrderByRestaurantId(int restaurantId, String orderState, Date orderDate, int from,
			int limit);

	Order addMoreFoods(ArrayList<CartModel> cartModels, int orderId);

}
