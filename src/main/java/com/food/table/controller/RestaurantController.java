package com.food.table.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.DefaultValuesResponse;
import com.food.table.model.RestaurantGetModel;
import com.food.table.model.RestaurantModel;
import com.food.table.model.SearchModel;
import com.food.table.service.RestaurantService;

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
	public @ResponseBody List<RestaurantGetModel> getAllRestaurant(@RequestParam(value = "latitude", required = false) String latitude,
			@RequestParam(value = "longitude", required = false) String longitude,@RequestParam(value = "from", required = false,defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false,defaultValue = "25") int limit,
			@RequestParam(value = "km", required = false) String km) {
		return restaurantService.getAllRestaurant(from,limit,latitude,longitude,km);
	}
	
	@ApiOperation(value="View a list of Drafted restaurants")
	@GetMapping("/getAllDrafted")
	public @ResponseBody List<RestaurantGetModel> getAllDraftedRestaurant(@RequestParam(value = "from", required = false,defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false,defaultValue = "25") int limit) {
		return restaurantService.getAllDraftedRestaurant(from,limit);
	}
	
	@ApiOperation(value="Add a new restaurant")
	@PostMapping("/add")
	public void addRestaurant(@ApiParam(value = "Restaurant object store in database table without id", required = false)@Valid@RequestBody RestaurantModel restaurantModel) {
		restaurantService.addRestaurant(restaurantModel);
	}
	
	@ApiOperation(value="Update a restaurant for the selected values which is required to update in restaurant object")
	@PutMapping("/update")
	public void updateRestaurantDetail(@ApiParam(value = "Restaurant Object with id  to update restaurant details", required = false)@Valid @RequestBody RestaurantModel restaurantModel) {
		restaurantService.updateRestaurant(restaurantModel);
	}
	
	@ApiOperation(value="Delete a restaurant")
	@DeleteMapping("/delete/{id}")
	public Boolean deleteRestaurant(@ApiParam(value = "Restaurant Id from which restaurant object will delete from database table", required = false) @PathVariable("id") int id) {
		return restaurantService.deleteRestaurant(id);
	}

	@ApiOperation(value="View a list of available restaurants by filtering with restaurant name")
	@GetMapping("/search/restaurantName/{restaurantName}")
	public @ResponseBody List<RestaurantGetModel> getRestaurantByRestaurantName(@RequestParam(value = "latitude", required = false) String latitude,
			@RequestParam(value = "longitude", required = false) String longitude,@RequestParam(value = "from", required = false,defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false,defaultValue ="25") int limit,@PathVariable("restaurantName") String restaurantName) {
		return restaurantService.getRestaurantByRestaurantName(restaurantName, from, limit, latitude, longitude);
	}
	
	@ApiOperation(value = "View a list of available restaurants by filtering")
	@GetMapping("/search/")
	public @ResponseBody List<RestaurantGetModel> getRestaurantByFilter(
			@RequestParam(value = "latitude", required = false) String latitude,
			@RequestParam(value = "longitude", required = false) String longitude,
			@RequestParam(value = "from", required = false, defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
			@RequestParam(value = "restaurantService", required = false) List<String> restaurantSer,
			@RequestParam(value = "restaurantSeating", required = false) List<String> restaurantSeating,
			@RequestParam(value = "restaurantCuisine", required = false) List<String> restaurantCuisine,
			@RequestParam(value = "restaurantType", required = false) List<String> restaurantType,
			@RequestParam(value = "restaurantDiet", required = false) List<String> restaurantDiet,
			@RequestParam(value = "km", required = false) String km) {
		return restaurantService.getRestaurantByFilter(latitude, longitude,from,limit, km, restaurantSer,restaurantSeating,restaurantCuisine,restaurantType,restaurantDiet);
	}
	
	@GetMapping("/getStaticValues/")
	public @ResponseBody DefaultValuesResponse getDefaultTableValues() {
		return restaurantService.getDefaultTableValues();
	}
}
