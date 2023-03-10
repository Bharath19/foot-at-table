package com.food.table.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.food.table.constant.PaymentStatusEnum;
import com.food.table.dto.Order;
import com.food.table.model.BasicRevenueModel;
import com.food.table.model.CartModel;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.model.OrderStateModel;
import com.food.table.model.PaymentDetail;

@Service
public interface OrderService {

	Order createOrder(OrderModel orderModel);

	OrderResponseModel getOrderById(int orderid);

	Map<String, Integer> updateOrderState(OrderStateModel orderModel, int orderId);

	List<OrderResponseModel> getOrderByUserId(int userId, List<String> orderState, Date orderDate, int from, int limit);

	BasicRevenueModel getBasicRevenue(int restaurantId, Date orderDate);

	List<OrderResponseModel> getOrderByOrderTypeName(int restaurantId, List<String> orderTypes, List<String> orderState,
			Date orderDate, int from, int limit);

	List<OrderResponseModel> getOrderByRestaurantTableIdAndType(int restaurantId, int restaurantTableId,
			String orderType, List<String> orderState, Date orderDate, int from, int limit);

	List<OrderResponseModel> getOrderByRestaurantId(int restaurantId, List<String> orderState, Date orderDate, int from,
			int limit);
	
	List<OrderResponseModel> getOrderByFiler(int restaurantId, int restaurantTableId, List<String> orderTypes,
			List<String> orderState, Date orderDate, int from, int limit);
	
	Map<String, Integer> addMoreFoods(ArrayList<CartModel> cartModels, int orderId);

	void updateOrderStateAfterPayment(Order order, PaymentStatusEnum paymrntState);

	PaymentDetail initiatePayment(int orderId, String couponCode);
	
	OrderResponseModel initiateBillRequest(int orderId, String couponCode) ;

}
