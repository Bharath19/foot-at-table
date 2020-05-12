package com.food.table.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.service.OrderService;

@RestController
public class OrdersController {

	@Autowired
	OrderService orderService;

	@PostMapping(value = "/orders")
	@ResponseBody
	public ResponseEntity<OrderResponseModel> createOrder(@RequestBody OrderModel orderModel) {
		return new ResponseEntity<OrderResponseModel>(orderService.getOrderById(orderService.createOrder(orderModel).getId()), HttpStatus.CREATED);
	}

	@PutMapping("/orders/{orderId}")
	@ResponseBody
	public ResponseEntity<OrderResponseModel> updateOrder(@RequestBody OrderModel orderModel, @PathVariable int orderId) {
		return new ResponseEntity<OrderResponseModel>(orderService.getOrderById(orderService.updateOrder(orderModel, orderId).getId()), HttpStatus.ACCEPTED);
	}

	@PutMapping("/orders/state/{orderId}")
	@ResponseBody
	public OrderResponseModel updateOrderState(@RequestBody OrderModel orderModel, @PathVariable int orderId) {
		return orderService.getOrderById(orderService.updateOrderState(orderModel, orderId).getId());
	}
	
	@GetMapping("/orders/{id}")
	public OrderResponseModel getOrderByID(@RequestParam(required = true) int id) {
		return orderService.getOrderById(id);
	}
	
	@GetMapping("/orders/user/{userId}")
	public ArrayList<OrderResponseModel> getOrderByUserID(@RequestParam(required = true) int userId) {
		return orderService.getOrderByUserId(userId);
	}
	

}
