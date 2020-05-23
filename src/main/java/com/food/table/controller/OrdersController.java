package com.food.table.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.food.table.service.CartService;
import com.food.table.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.food.table.constant.OrderStateEnum;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.BasicRevenueModel;
import com.food.table.model.CartModel;
import com.food.table.model.FoodResponseModel;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.model.OrderStateModel;

@RestController
public class OrdersController {

	@Autowired
	OrderService orderService;

	@Autowired
	CartService cartService;

	@ApiOperation(value = "Create a new order", authorizations = { @Authorization(value = "accessToken") })
	@PostMapping(value = "/orders")
	public @ResponseBody ResponseEntity<OrderResponseModel> createOrder(@RequestBody OrderModel orderModel) {
		return new ResponseEntity<OrderResponseModel>(
				orderService.getOrderById(orderService.createOrder(orderModel).getId()), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Add more food on dineIn/live cart", authorizations = {
			@Authorization(value = "accessToken") })
	@PutMapping("/orders/addMoreFoods/{orderId}")
	@ResponseBody
	public ResponseEntity<OrderResponseModel> addMoreFoods(@RequestBody ArrayList<CartModel> cartModels,
			@PathVariable int orderId) {
		return new ResponseEntity<OrderResponseModel>(
				orderService.getOrderById(orderService.addMoreFoods(cartModels, orderId).getId()), HttpStatus.ACCEPTED);
	}

	@ApiOperation(value = "Update a restaurantTableId, Food quantity and Cart status", authorizations = {
			@Authorization(value = "accessToken") })
	@PutMapping("/orders/{orderId}")
	@ResponseBody
	public ResponseEntity<OrderResponseModel> updateOrder(@RequestBody OrderModel orderModel,
			@PathVariable int orderId) {
		return new ResponseEntity<OrderResponseModel>(
				orderService.getOrderById(orderService.updateOrder(orderModel, orderId).getId()), HttpStatus.ACCEPTED);
	}

	@ApiOperation(value = "Here we can update order and cart<one or more> state. If we need to update order state as COMPLETED, SERVED or CANCELLED, respect all the cart states are updated automatically. Otherwise need to update order and cart state separately", authorizations = {
			@Authorization(value = "accessToken") })
	@PutMapping("/orders/state/{orderId}")
	@ResponseBody
	public OrderResponseModel updateOrderState(@RequestBody OrderStateModel orderStateModel,
			@PathVariable int orderId) {
		return orderService.getOrderById(orderService.updateOrderState(orderStateModel, orderId).getId());
	}

	@ApiOperation(value = "Get Order details by order ID", authorizations = { @Authorization(value = "accessToken") })
	@GetMapping("/orders/{id}")
	public OrderResponseModel getOrderByID(@RequestParam(required = true) int id) {
		return orderService.getOrderById(id);
	}

	@ApiOperation(value = "Get Order details by User ID", authorizations = { @Authorization(value = "accessToken") })
	@GetMapping("/orders/getOrderByUserId/{userId}")
	public List<OrderResponseModel> getOrderByUserID(@RequestParam(required = true) int userId,
			@RequestParam(value = "orderState", required = false) String orderState,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
			@RequestParam(value = "from", required = false, defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		try {
			if (orderState != null) {
				OrderStateEnum.valueOf(orderState);
			}
		} catch (IllegalArgumentException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE);
		}
		return orderService.getOrderByUserId(userId, orderState, orderDate, from, limit);
	}

	@ApiOperation(value = "Get all food history by RestaurantId and order date. Order date should be yyyy-MM-dd format", authorizations = {
			@Authorization(value = "accessToken") })
	@GetMapping("/orders/foodHistory/{restaurantId}")
	public ArrayList<FoodResponseModel> getFoodHistoryByRestaurantId(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "orderDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate) {
		return cartService.getFoodHistoryByRestaurantId(restaurantId, orderDate);
	}

	@ApiOperation(value = "Get Revenue details of the restaurant. OrderDate should be yyyy-MM-dd format", authorizations = {
			@Authorization(value = "accessToken") })
	@GetMapping("/orders/revenueDetails/{restaurantId}")
	public BasicRevenueModel getBasicRevenue(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "orderDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate) {
		return orderService.getBasicRevenue(restaurantId, orderDate);
	}

	@ApiOperation(value = "Get order details by restaurantId, OrderTypes, orderState and orderDate. Here orderState, orderDate, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {
			@Authorization(value = "accessToken") })
	@GetMapping("/orders/getOrderByOrderTypeName/{restaurantId}")
	public List<OrderResponseModel> getOrderByOrderTypeName(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "orderTypeId", required = true) List<String> orderTypes,
			@RequestParam(value = "orderState", required = false) String orderState,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
			@RequestParam(value = "from", required = false, defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		try {
			if (orderState != null) {
				OrderStateEnum.valueOf(orderState);
			}
		} catch (IllegalArgumentException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE);
		}
		return orderService.getOrderByOrderTypeName(restaurantId, orderTypes, orderState, orderDate, from, limit);
	}

	@ApiOperation(value = "Get order details by restaurantId, RestaurantTableId, orderState and orderDate. Here orderState, orderDate, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {
			@Authorization(value = "accessToken") })
	@GetMapping("/orders/getOrderByRestaurantTableId/{restaurantId}")
	public List<OrderResponseModel> getOrderByRestaurantTableId(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "restaurantTableId", required = true) int restaurantTableId,
			@RequestParam(value = "orderState", required = false) String orderState,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
			@RequestParam(value = "from", required = false, defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		try {
			if (orderState != null) {
				OrderStateEnum.valueOf(orderState);
			}
		} catch (IllegalArgumentException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE);
		}
		return orderService.getOrderByRestaurantTableId(restaurantId, restaurantTableId, orderState, orderDate, from,
				limit);
	}

	@ApiOperation(value = "Get order details by restaurantId, orderState and orderDate. Here orderState, orderDate, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {
			@Authorization(value = "accessToken") })
	@GetMapping("/orders/getOrderByRestaurantId/{restaurantId}")
	public List<OrderResponseModel> getOrderByRestaurantId(@RequestParam(required = true) int restaurantId,
			@RequestParam(value = "orderState", required = false) String orderState,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
			@RequestParam(value = "from", required = false, defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		try {
			if (orderState != null) {
				OrderStateEnum.valueOf(orderState);
			}
		} catch (IllegalArgumentException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE);
		}
		return orderService.getOrderByRestaurantId(restaurantId, orderState, orderDate, from, limit);
	}

}
