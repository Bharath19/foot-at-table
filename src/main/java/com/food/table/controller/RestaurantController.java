package com.food.table.controller;

import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.dto.Restaurant;
import com.food.table.model.RestaurantGetModel;
import com.food.table.model.RestaurantModel;
import com.food.table.service.RestaurantService;
import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/restaurant")
@Api(value="Restaurant Management System")
public class RestaurantController {
	
	@Autowired
	RestaurantService restaurantService;
	
	@ApiOperation(value="View a list of available restaurants")
	@GetMapping("/getAllConfirmed")
	public @ResponseBody List<RestaurantGetModel> getAllRestaurant() {
		return restaurantService.getAllRestaurant();
	}
	
	@ApiOperation(value="Add a new restaurant")
	@PostMapping("/add")
	public void addRestaurant(@ApiParam(value = "Restaurant object store in database table without id", required = true)@Valid@RequestBody RestaurantModel restaurantModel) {
		restaurantService.addRestaurant(restaurantModel);
	}
	
	@ApiOperation(value="Update a restaurant for the selected values which is required to update in restaurant object")
	@PutMapping("/update")
	public void updateRestaurantDetail(@ApiParam(value = "Restaurant Object with id  to update restaurant details", required = true)@Valid @RequestBody RestaurantModel restaurantModel) {
		restaurantService.updateRestaurant(restaurantModel);
	}
	
	@ApiOperation(value="Delete a restaurant")
	@DeleteMapping("/delete/{id}")
	public Boolean deleteRestaurant(@ApiParam(value = "Restaurant Id from which restaurant object will delete from database table", required = true) @PathVariable("id") int id) {
		return restaurantService.deleteRestaurant(id);
	}
	
	
}
