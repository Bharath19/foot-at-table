package com.food.table.controller;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.constant.ApplicationConstants;
import com.food.table.constant.CartStateEnum;
import com.food.table.constant.OrderStateEnum;
import com.food.table.model.BasicRevenueModel;
import com.food.table.model.CartModel;
import com.food.table.model.FoodHistoryProjectionModel;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.model.OrderStateModel;
import com.food.table.model.PaymentDetail;
import com.food.table.service.CartService;
import com.food.table.service.OrderService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrdersController {

	@Autowired
	OrderService orderService;

	@Autowired
	CartService cartService;

	@ApiOperation(value = "Create a new order", authorizations = { @Authorization(value = "accessToken") })
	@PostMapping(value = "/createOrder")
	public ResponseEntity<Map<String, Integer>> createOrder(@RequestBody OrderModel orderModel) {
		long startTime=System.currentTimeMillis();
		log.info("Entering create new order starttime : "+startTime);
		ResponseEntity<Map<String, Integer>> response = new ResponseEntity<Map<String, Integer>>(Collections.singletonMap("OrderId", orderService.createOrder(orderModel).getId()), HttpStatus.CREATED);
		long endTime=System.currentTimeMillis();
		log.info("Exiting create new order is success and timetaken : "+(endTime-startTime));
		return response;
	}

	@ApiOperation(value = "Add more food on dineIn/live cart", authorizations = {@Authorization(value = "accessToken") })
	@PutMapping("/addMoreFoods/{orderId}")
	public ResponseEntity<Map<String, Integer>> addMoreFoods(@PathVariable int orderId, @RequestBody ArrayList<CartModel> cartModels) {
		long startTime=System.currentTimeMillis();
		log.info("Entering add more food starttime : "+startTime);
		Map<String, Integer> response = orderService.addMoreFoods(cartModels, orderId);
		long endTime=System.currentTimeMillis();
		log.info("Exiting add more food is success and timetaken : "+(endTime-startTime));
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "Here we can update order and cart<one or more> state. If we need to update order state as COMPLETED, SERVED or CANCELLED, respect all the cart states are updated automatically. Otherwise need to update order and cart state separately", authorizations = {@Authorization(value = "accessToken") })
	@PutMapping("/updateState/{orderId}")
	@ResponseBody
	public ResponseEntity<Map<String, Integer>> updateOrderState(@PathVariable int orderId, @RequestBody OrderStateModel orderStateModel) {
		long startTime=System.currentTimeMillis();
		log.info("Entering update order and cart state starttime : "+startTime);
		Map<String, Integer> responseObject= orderService.updateOrderState(orderStateModel, orderId);
		ResponseEntity<Map<String, Integer>> response = new ResponseEntity<Map<String, Integer>>(responseObject, HttpStatus.OK);
		long endTime=System.currentTimeMillis();
		log.info("Exiting update order and cart state is success and timetaken : "+(endTime-startTime));
		return response;
	}
	
	@ApiOperation(value = "Initiate Bil Request for order. Here, order automatically moved to BIL_REQUESTED state", authorizations = {@Authorization(value = "accessToken") })
	@PutMapping("/billRequest/{orderId}")
	@ResponseBody
	public ResponseEntity<Map<String, OrderResponseModel>> billRequest(@PathVariable int orderId,  @RequestParam(value = "couponCode", required = false) String couponCode) {
		long startTime=System.currentTimeMillis();
		log.info("Entering initiate bill request starttime : "+startTime);
		OrderResponseModel orderResponseModel = orderService.initiateBillRequest(orderId, couponCode);
		Map<String, OrderResponseModel> responseObject = Collections.singletonMap("orderResponseModel", orderResponseModel);
		ResponseEntity<Map<String, OrderResponseModel>> response = new ResponseEntity<Map<String, OrderResponseModel>>(responseObject, HttpStatus.OK);
		long endTime=System.currentTimeMillis();
		log.info("Exiting initiate bill request is success and timetaken : "+(endTime-startTime));
		return response;
	}
	
	@ApiOperation(value = "Initiate payment Request for order. Return the Payment response model to call payment gateway", authorizations = {@Authorization(value = "accessToken") })
	@PutMapping("/initiatePayment/{orderId}")
	@ResponseBody
	public ResponseEntity<Map<String, PaymentDetail>> initiatePayment(@PathVariable int orderId,  @RequestParam(value = "couponCode", required = false) String couponCode) {
		long startTime=System.currentTimeMillis();
		log.info("Entering initiate bill request starttime : "+startTime);
		PaymentDetail paymentDetail = orderService.initiatePayment(orderId, couponCode);
		Map<String, PaymentDetail> responseObject = Collections.singletonMap("paymentDetail", paymentDetail);
		ResponseEntity<Map<String, PaymentDetail>> response = new ResponseEntity<Map<String, PaymentDetail>>(responseObject, HttpStatus.OK);
		long endTime=System.currentTimeMillis();
		log.info("Exiting initiate bill request is success and timetaken : "+(endTime-startTime));
		return response;
	}

	@ApiOperation(value = "Get Order details by order ID", authorizations = { @Authorization(value = "accessToken") })
	@GetMapping("/{id}")
	public OrderResponseModel getOrderByID(@PathVariable(required = true) int id) {
		long startTime=System.currentTimeMillis();
		log.info("Entering get order details starttime : "+startTime);
		OrderResponseModel orderResponse = orderService.getOrderById(id);
		long endTime=System.currentTimeMillis();
		log.info("Exiting get order details is success and timetaken : "+(endTime-startTime));
		return orderResponse;
	}

	@ApiOperation(value = "Get Order details by User ID. Here user ID is optional param. If we did not pass the user ID, current user order details will be displayed", authorizations = { @Authorization(value = "accessToken") })
	@GetMapping("/getOrderByUserId")
	public List<OrderResponseModel> getOrderByUserID(@RequestParam(value = "userId", required = false, defaultValue = "0") int userId, @RequestParam(value = "orderState", required = false) List<String> orderState, @RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
			@RequestParam(value = "from", required = false, defaultValue = "0") int from, @RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		long startTime=System.currentTimeMillis();
		log.info("Entering get order details by userID starttime : "+startTime);
		List<OrderResponseModel> orderResponse = orderService.getOrderByUserId(userId, orderState, orderDate, from, limit);
		long endTime=System.currentTimeMillis();
		log.info("Exiting get order details by userID is success and timetaken : "+(endTime-startTime));
		return orderResponse;
	}

	@ApiOperation(value = "Get all food history by RestaurantId and order date. Order date should be yyyy-MM-dd format", authorizations = { @Authorization(value = "accessToken") })
	@GetMapping("/menuMovement/{restaurantId}")
	public List<FoodHistoryProjectionModel> getFoodHistoryByRestaurantId( @PathVariable(required = true) int restaurantId, @RequestParam(value = "orderDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate) {
		long startTime=System.currentTimeMillis();
		log.info("Entering food history starttime : "+startTime);
		List<FoodHistoryProjectionModel> foodHistoryResponse = cartService.getFoodHistoryByRestaurantId(restaurantId, orderDate);
		long endTime=System.currentTimeMillis();
		log.info("Exiting food history is success and timetaken : "+(endTime-startTime));
		return foodHistoryResponse;
	}

	@ApiOperation(value = "Get Revenue details of the restaurant. OrderDate should be yyyy-MM-dd format", authorizations = {@Authorization(value = "accessToken") })
	@GetMapping("/revenueDetails/{restaurantId}")
	public BasicRevenueModel getBasicRevenue(@PathVariable(required = true) int restaurantId, @RequestParam(value = "orderDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate) {
		long startTime=System.currentTimeMillis();
		log.info("Entering revenue details starttime : "+startTime);
		BasicRevenueModel basicRevenueModel =  orderService.getBasicRevenue(restaurantId, orderDate);
		long endTime=System.currentTimeMillis();
		log.info("Exiting revenue details is success and timetaken : "+(endTime-startTime));
		return basicRevenueModel;
	}

	@Deprecated
	@ApiOperation(value = "Get order details by restaurantId, OrderTypes, orderState and orderDate. Here orderState, orderDate, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {@Authorization(value = "accessToken") })
	@GetMapping("/getOrderByOrderTypeName/{restaurantId}")
	public List<OrderResponseModel> getOrderByOrderTypeName(@PathVariable(required = true) int restaurantId, @RequestParam(value = "orderTypes", required = true) List<String> orderTypes, @RequestParam(value = "orderState", required = false) List<String> orderState,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate, @RequestParam(value = "from", required = false, defaultValue = "0") int from, 
			@RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		long startTime=System.currentTimeMillis();
		log.info("Entering order details by restaurant starttime : "+startTime);
		List<OrderResponseModel> responseList = orderService.getOrderByOrderTypeName(restaurantId, orderTypes, orderState, orderDate, from, limit);
		long endTime=System.currentTimeMillis();
		log.info("Exiting order details by restaurant is success and timetaken : "+(endTime-startTime));
		return responseList;
	}
	
	@ApiOperation(value = "Get completed order details by restaurantId, restaurantTableId, OrderTypes and orderDate. Here orderDate, restaurantTableId, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {@Authorization(value = "accessToken") })
	@GetMapping("/getCompletedOrderByFiler/{restaurantId}")
	public List<OrderResponseModel> getCompletedOrderByFiler(@PathVariable(required = true) int restaurantId, @RequestParam(value = "orderTypes", required = false) List<String> orderTypes,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate, @RequestParam(value = "restaurantTableId", required = false, defaultValue="0") int restaurantTableId, 
			@RequestParam(value = "from", required = false, defaultValue = "0") int from, @RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		long startTime=System.currentTimeMillis();
		log.info("Entering completed order details by restaurant starttime : "+startTime);
		List<String> orderState = Arrays.asList("COMPLETED");
		List<OrderResponseModel> responseList = orderService.getOrderByFiler(restaurantId, restaurantTableId, orderTypes, orderState, orderDate, from, limit);
		long endTime=System.currentTimeMillis();
		log.info("Exiting completed order details by restaurant is success and timetaken : "+(endTime-startTime));
		return responseList;
	}
	
	@ApiOperation(value = "Get order details by restaurantId, restaurantTableId, OrderTypes, orderState and orderDate. Here orderState, orderDate, restaurantTableId, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {@Authorization(value = "accessToken") })
	@GetMapping("/getOrderByFiler/{restaurantId}")
	public List<OrderResponseModel> getOrderByFiler(@PathVariable(required = true) int restaurantId, @RequestParam(value = "orderTypes", required = false) List<String> orderTypes, @RequestParam(value = "orderState", required = false) List<String> orderState,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate, @RequestParam(value = "restaurantTableId", required = false, defaultValue="0") int restaurantTableId, 
			@RequestParam(value = "from", required = false, defaultValue = "0") int from, @RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		long startTime=System.currentTimeMillis();
		log.info("Entering filter order details by restaurant starttime : "+startTime);
		List<OrderResponseModel> responseList = orderService.getOrderByFiler(restaurantId, restaurantTableId, orderTypes, orderState, orderDate, from, limit);
		long endTime=System.currentTimeMillis();
		log.info("Exiting filter order details by restaurant is success and timetaken : "+(endTime-startTime));
		return responseList;
	}

	@ApiOperation(value = "Get order details by restaurantId, RestaurantTableId, orderState and orderDate. Here orderState, orderDate, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {
			@Authorization(value = "accessToken") })
	@GetMapping("/getDineInOrders/{restaurantId}")
	public List<OrderResponseModel> getDineInOrders(@PathVariable(required = true) int restaurantId, @RequestParam(value = "restaurantTableId", required = true) int restaurantTableId, @RequestParam(value = "orderState", required = false) List<String> orderState,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate, @RequestParam(value = "from", required = false, defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		long startTime=System.currentTimeMillis();
		log.info("Entering order details by dineIn type starttime : "+startTime);
		List<OrderResponseModel> orderResponse = orderService.getOrderByRestaurantTableIdAndType(restaurantId, restaurantTableId, ApplicationConstants.dineInTypeText, orderState, orderDate, from, limit);
		long endTime=System.currentTimeMillis();
		log.info("Exiting order details by dineIn type is success and timetaken : "+(endTime-startTime));
		return orderResponse;
	}

	@ApiOperation(value = "Get order details by restaurantId, RestaurantTableId, orderState and orderDate. Here orderState, orderDate, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {
			@Authorization(value = "accessToken") })
	@GetMapping("/getSelfServiceOrders/{restaurantId}")
	public List<OrderResponseModel> getSelfServiceOrders(@PathVariable(required = true) int restaurantId, @RequestParam(value = "restaurantTableId", required = true) int restaurantTableId, @RequestParam(value = "orderState", required = false) List<String> orderState,
			@RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate, @RequestParam(value = "from", required = false, defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		long startTime=System.currentTimeMillis();
		log.info("Entering order details by self-service type starttime : "+startTime);
		List<OrderResponseModel> orderResponse = orderService.getOrderByRestaurantTableIdAndType(restaurantId, restaurantTableId, ApplicationConstants.selfServiceTypeText, orderState, orderDate, from, limit);
		long endTime=System.currentTimeMillis();
		log.info("Exiting order details by self-service type is success and timetaken : "+(endTime-startTime));
		return orderResponse;
	}

	@ApiOperation(value = "Get order details by restaurantId, orderState and orderDate. Here orderState, orderDate, from and limit parameters are optional and orderDate should be yyyy-MM-dd format", authorizations = {
			@Authorization(value = "accessToken") })
	@GetMapping("/getOrderByRestaurantId/{restaurantId}")
	public List<OrderResponseModel> getOrderByRestaurantId(@PathVariable(required = true) int restaurantId, @RequestParam(value = "orderState", required = false) List<String> orderState, @RequestParam(value = "orderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
			@RequestParam(value = "from", required = false, defaultValue = "0") int from, @RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		long startTime=System.currentTimeMillis();
		log.info("Entering order details by RestaurantId starttime : "+startTime);
		List<OrderResponseModel> orderResponse = orderService.getOrderByRestaurantId(restaurantId, orderState, orderDate, from, limit);
		long endTime=System.currentTimeMillis();
		log.info("Exiting order details by RestaurantId is success and timetaken : "+(endTime-startTime));
		return orderResponse;
	}

	@ApiOperation(value = "Get static values", authorizations = { @Authorization(value = "accessToken") })
	@GetMapping("/getStaticValues")
	public Map<String, Object> getStaticValues() {
		long startTime=System.currentTimeMillis();
		log.info("Entering static value starttime : "+startTime);
		Map<String, Object> staticValues = Map.ofEntries(entry("OrderStates", OrderStateEnum.values()), entry("CartStates", CartStateEnum.values()), entry("OrderType", ApplicationConstants.types));
		long endTime=System.currentTimeMillis();
		log.info("Exiting static value is success and timetaken : "+(endTime-startTime));
		return staticValues;
	}

}
