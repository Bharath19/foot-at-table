package com.food.table.controller;

import com.food.table.model.*;
import com.food.table.service.CartService;
import com.food.table.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
public class OrdersController {

	@Autowired
	OrderService orderService;

	@Autowired
	CartService cartService;

	@ApiOperation(value = "Create a new order", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping(value = "/orders")
	public @ResponseBody ResponseEntity<OrderResponseModel> createOrder(@RequestBody OrderModel orderModel) {
		return new ResponseEntity<OrderResponseModel>(
				orderService.getOrderById(orderService.createOrder(orderModel).getId()), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update a order", authorizations = {@Authorization(value = "accessToken")})
	@PutMapping("/orders/{orderId}")
	@ResponseBody
	public ResponseEntity<OrderResponseModel> updateOrder(@RequestBody OrderModel orderModel,
														  @PathVariable int orderId) {
		return new ResponseEntity<OrderResponseModel>(
				orderService.getOrderById(orderService.updateOrder(orderModel, orderId).getId()), HttpStatus.ACCEPTED);
	}

	@ApiOperation(value = "Here we can update order and cart<one or more> state. If we need to update order state as COMPLETED, SERVED or CANCELLED, respect all the cart states are updated automatically. Otherwise need to update order and cart state separately", authorizations = {@Authorization(value = "accessToken")})
	@PutMapping("/orders/state/{orderId}")
	@ResponseBody
	public OrderResponseModel updateOrderState(@RequestBody OrderStateModel orderStateModel,
			@PathVariable int orderId) {
		return orderService.getOrderById(orderService.updateOrderState(orderStateModel, orderId).getId());
	}

	@ApiOperation(value = "Get Order details by order ID", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/orders/{id}")
	public OrderResponseModel getOrderByID(@RequestParam(required = true) int id) {
		return orderService.getOrderById(id);
	}

	@GetMapping("/orders/user/{userId}")
	public ArrayList<OrderResponseModel> getOrderByUserID(@RequestParam(required = true) int userId) {
		return orderService.getOrderByUserId(userId);
	}

	@ApiOperation(value = "Get all food history by RestaurantId and order date. Order date should be yyyy-MM-dd format", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/orders/foodHistory/{restaurantId}")
	public ArrayList<FoodResponseModel> getFoodHistoryByRestaurantId(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "orderDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate) {
		return cartService.getFoodHistoryByRestaurantId(restaurantId, orderDate);
	}

	@ApiOperation(value = "Get Revenue details by order type. Order date should be yyyy-MM-dd format", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/orders/revenueDetails/{restaurantId}")
	public BasicRevenueModel getBasicRevenue(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "orderDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate) {
		return orderService.getBasicRevenue(restaurantId, orderDate);
	}

}
