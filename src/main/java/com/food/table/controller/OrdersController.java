package com.food.table.controller;

import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.BasicRevenueModel;
import com.food.table.model.FoodResponseModel;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.model.OrderStateModel;
import com.food.table.service.CartService;
import com.food.table.service.OrderService;

import io.swagger.annotations.ApiOperation;

@RestController
public class OrdersController {

	@Autowired
	OrderService orderService;

	@Autowired
	CartService cartService;

	@PostMapping(value = "/orders")
	public @ResponseBody ResponseEntity<OrderResponseModel> createOrder(@RequestBody OrderModel orderModel) {
		return new ResponseEntity<OrderResponseModel>(
				orderService.getOrderById(orderService.createOrder(orderModel).getId()), HttpStatus.CREATED);
	}

	@PutMapping("/orders/{orderId}")
	@ResponseBody
	public ResponseEntity<OrderResponseModel> updateOrder(@RequestBody OrderModel orderModel,
			@PathVariable int orderId) {
		return new ResponseEntity<OrderResponseModel>(
				orderService.getOrderById(orderService.updateOrder(orderModel, orderId).getId()), HttpStatus.ACCEPTED);
	}

	@ApiOperation(value = "Here we can update order and cart<one or more> state. If we need to update order state as COMPLETED, SERVED or CANCELLED, respect all the cart states are updated automatically. Otherwise need to update order and cart state separately")
	@PutMapping("/orders/state/{orderId}")
	@ResponseBody
	public OrderResponseModel updateOrderState(@RequestBody OrderStateModel orderStateModel,
			@PathVariable int orderId) {
		return orderService.getOrderById(orderService.updateOrderState(orderStateModel, orderId).getId());
	}

	@ApiOperation(value = "Get Order details by order ID")
	@GetMapping("/orders/{id}")
	public OrderResponseModel getOrderByID(@RequestParam(required = true) int id) {
		return orderService.getOrderById(id);
	}

	@GetMapping("/orders/user/{userId}")
	public ArrayList<OrderResponseModel> getOrderByUserID(@RequestParam(required = true) int userId) {
		return orderService.getOrderByUserId(userId);
	}

	@ApiOperation(value = "Get all food history by RestaurantId and order date. Order date should be yyyy-MM-dd format")
	@GetMapping("/orders/foodHistory/{restaurantId}")
	public ArrayList<FoodResponseModel> getFoodHistoryByRestaurantId(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "orderDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate) {
		return cartService.getFoodHistoryByRestaurantId(restaurantId, orderDate);
	}

	@ApiOperation(value = "Get Revenue details by order type. Order date should be yyyy-MM-dd format")
	@GetMapping("/orders/revenueDetails/{restaurantId}")
	public BasicRevenueModel getBasicRevenue(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "orderDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate) {
		return orderService.getBasicRevenue(restaurantId, orderDate);
	}

}
