package com.food.table.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.FavoriteRestaurantModel;
import com.food.table.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Api(value = "User Management System")
@Slf4j
public class UsersController {

	@Autowired
	UserService userService;

	@ApiOperation(value = "Add a favorite restaurant", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/addFavoriteRestaurant")
	public ResponseEntity<Void> addFavoriteRestaurant(@RequestParam(value = "restaurantId", required = false) int restaurantId) {
		long startTime = System.currentTimeMillis();
		log.info("Entering add a favorite restaurant start time : " + startTime);
		userService.addFavoriteRestaurant(restaurantId);
		long endTime = System.currentTimeMillis();
		log.info("Exiting add a favorite restaurant is success and timetaken : " + (endTime - startTime));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Delete a favorite restaurant", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/deleteFavoriteRestaurant")
	public ResponseEntity<Void> deleteFavoriteRestaurant(@RequestParam(value = "restaurantId", required = false) int restaurantId) {
		long startTime = System.currentTimeMillis();
		log.info("Entering add a favorite restaurant start time : " + startTime);
		userService.deleteFavoriteRestaurant(restaurantId);
		long endTime = System.currentTimeMillis();
		log.info("Exiting add a favorite restaurant is success and timetaken : " + (endTime - startTime));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(value = "Add a favorite restaurant", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/getAllFavoriteRestaurant")
	public List<FavoriteRestaurantModel> getAllFavoriteRestaurant( @RequestParam(value = "latitude", required = false) String latitude, @RequestParam(value = "longitude", required = false) String longitude, 
			@RequestParam(value = "from", required = false, defaultValue = "0") int from, @RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
		long startTime = System.currentTimeMillis();
		log.info("Entering get all favorite restaurant start time : " + startTime);
		List<FavoriteRestaurantModel> allFavoriteRestaurant = userService.getAllFavoriteRestaurant(latitude, longitude, from, limit);
		long endTime = System.currentTimeMillis();
		log.info("Exiting get all favorite restaurant is success and timetaken : " + (endTime - startTime));
		return allFavoriteRestaurant;
	}
}
