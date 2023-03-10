package com.food.table.controller;

import com.food.table.model.*;
import com.food.table.service.RestaurantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restaurant")
@Api(value="Restaurant Management System")
@Slf4j
public class RestaurantController {
	
	@Autowired
	RestaurantService restaurantService;

	@ApiOperation(value = "View a list of available restaurants", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/getAllConfirmed")
	public ResponseEntity<List<RestaurantGetModel>> getAllRestaurant(@RequestParam(value = "latitude", required = false) String latitude,
			@RequestParam(value = "longitude", required = false) String longitude,@RequestParam(value = "from", required = false,defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false,defaultValue = "25") int limit,
			@RequestParam(value = "km", required = false) String km) {
		long startTime=System.currentTimeMillis();
		log.info("Entering get All restaurant starttime : "+startTime);
		List<RestaurantGetModel> restaurantGetModelResponse = restaurantService.getAllRestaurant(from,limit,latitude,longitude,km);
		long endTime=System.currentTimeMillis();
		log.info("Exiting get All restaurant is success and timetaken : "+(endTime-startTime));
		return ResponseEntity.ok(restaurantGetModelResponse);
	}

	@ApiOperation(value = "View a list of Drafted restaurants", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/getAllDrafted")
	public ResponseEntity<List<RestaurantGetModel>> getAllDraftedRestaurant(@RequestParam(value = "from", required = false,defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false,defaultValue = "25") int limit) {
		long startTime=System.currentTimeMillis();
		log.info("Entering get All drafted restaurant starttime : "+startTime);
		List<RestaurantGetModel> restaurantGetModel = restaurantService.getAllDraftedRestaurant(from,limit);
		long endTime=System.currentTimeMillis();
		log.info("Exiting get All drafted restaurant is success and timetaken : "+(endTime-startTime));
		return ResponseEntity.ok(restaurantGetModel);
	}

    @ApiOperation(value = "Add a new restaurant")
	@PostMapping("/add")
	public ResponseEntity<Void> addRestaurant(@ApiParam(value = "Restaurant object store in database table without id", required = false)@Valid@RequestBody RestaurantModel restaurantModel) {
		long startTime=System.currentTimeMillis();
		log.info("Entering add restaurant starttime : "+startTime);
		restaurantService.addRestaurant(restaurantModel);
		long endTime=System.currentTimeMillis();
		log.info("Exiting add restaurant is success and timetaken : "+(endTime-startTime));		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update a restaurant for the selected values which is required to update in restaurant object", authorizations = {@Authorization(value = "accessToken")})
	@PutMapping("/update")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<Void> updateRestaurantDetail(@ApiParam(value = "Restaurant Object with id  to update restaurant details", required = false)@Valid @RequestBody RestaurantModel restaurantModel) {
		long startTime=System.currentTimeMillis();
		log.info("Entering update restaurant detail starttime : "+startTime);
		restaurantService.updateRestaurant(restaurantModel);
		long endTime=System.currentTimeMillis();
		log.info("Exiting update restaurant detail is success and timetaken : "+(endTime-startTime));		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Delete a restaurant", authorizations = {@Authorization(value = "accessToken")})
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<Void> deleteRestaurant(@ApiParam(value = "Restaurant Id from which restaurant object will delete from database table", required = false) @PathVariable("id") int id) {
		long startTime=System.currentTimeMillis();
		log.info("Entering delete restaurant for :"+ id +"starttime : "+startTime);
		restaurantService.deleteRestaurant(id);
		long endTime=System.currentTimeMillis();
		log.info("Exiting delete restaurant is success for :"+ id +"and timetaken : "+(endTime-startTime));		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "View a list of available restaurants by filtering with restaurant name", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/search/restaurantName/{restaurantName}")
	public ResponseEntity<List<RestaurantGetModel>> getRestaurantByRestaurantName(@RequestParam(value = "latitude", required = false) String latitude,
			@RequestParam(value = "longitude", required = false) String longitude,@RequestParam(value = "from", required = false,defaultValue = "0") int from,
			@RequestParam(value = "limit", required = false,defaultValue ="25") int limit,@PathVariable("restaurantName") String restaurantName) {
		
		long startTime=System.currentTimeMillis();
		log.info("Entering get restaurant by name starttime : "+startTime);
		List<RestaurantGetModel> restaurantGetModel =  restaurantService.getRestaurantByRestaurantName(restaurantName, from, limit, latitude, longitude);
		long endTime=System.currentTimeMillis();
		log.info("Exiting get restaurant by name is success and timetaken : "+(endTime-startTime));
		return ResponseEntity.ok(restaurantGetModel);
	}

	@ApiOperation(value = "View a list of available restaurants by filtering", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/search/")
	public ResponseEntity<List<RestaurantGetModel>> getRestaurantByFilter(
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
		long startTime=System.currentTimeMillis();
		log.info("Entering get All restaurant by filter starttime : "+startTime);
		List<RestaurantGetModel> restaurantGetModel = restaurantService.getRestaurantByFilter(latitude, longitude,from,limit, km, restaurantSer,restaurantSeating,restaurantCuisine,restaurantType,restaurantDiet);
		long endTime=System.currentTimeMillis();
		log.info("Exiting get All restaurant by filter is success and timetaken : "+(endTime-startTime));
		return ResponseEntity.ok(restaurantGetModel);
	}

	@ApiOperation(value = "Get static values of restaurant")
	@GetMapping("/getStaticValues/")
	public ResponseEntity<DefaultValuesResponse> getDefaultTableValues() {
		long startTime = System.currentTimeMillis();
		log.info("Entering get all default values for restaurant starttime : " + startTime);
		DefaultValuesResponse defaultValuesResponse = restaurantService.getDefaultTableValues();
		long endTime = System.currentTimeMillis();
		log.info("Entering get all default values for restaurant timetaken : " + (endTime - startTime));
		return ResponseEntity.ok(defaultValuesResponse);
	}

	@ApiOperation(value = "Update state of the restaurant", authorizations = {@Authorization(value = "accessToken")})
	@PutMapping("/updateStateAndStatus/{id}")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<Void> updateState(@PathVariable(required = true) int id,  @Valid @RequestBody RestaurantUpdateRequest restaurantUpdateRequest) {
		long startTime=System.currentTimeMillis();
		log.info("Entering update restaurant state starttime : "+startTime);
		restaurantService.updateStateAndStatus(id, restaurantUpdateRequest);
		long endTime=System.currentTimeMillis();
		log.info("Exiting update restaurant state and timetaken : "+(endTime-startTime));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

		
	@ApiOperation(value = "Get Restaurant Timings", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/timings")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<List<TimingModel>> getRestaurantTimings(@RequestParam(value = "restaurantId") int restaurantId) {
		long startTime=System.currentTimeMillis();
		log.info("Entering get timings for restaurant starttime : "+startTime);
		long endTime=System.currentTimeMillis();
		List<TimingModel> timingmodel = restaurantService.getRestaurantTimings(restaurantId);
		log.info("Exiting  get timings for restaurant is success and timetaken : "+(endTime-startTime));
		return ResponseEntity.ok(timingmodel);
	}
	
	@ApiOperation(value = "Get restaurant details", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/{restaurantId}")
	public ResponseEntity<RestaurantGetModel> getRestaurantById(@PathVariable(required = true) int restaurantId) {
		long startTime=System.currentTimeMillis();
		log.info("Entering get timings for restaurant starttime : "+startTime);
		long endTime=System.currentTimeMillis();
		RestaurantGetModel restaurantGetModel = restaurantService.getRestaurantById(restaurantId);
		log.info("Exiting  get timings for restaurant is success and timetaken : "+(endTime-startTime));
		return ResponseEntity.ok(restaurantGetModel);
		
	}
	
	@ApiOperation(value = "Get restaurant details", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/users/{restaurantId}")
	public ResponseEntity<List<UserProfileResponseModel>> getRestaurantUsers(@PathVariable(required = true) int restaurantId) {
		log.info("Entering get users for restaurant  : "+restaurantId);
		List<UserProfileResponseModel> userList = restaurantService.getRestaurantUsers(restaurantId);
		log.info("Exiting get users for restaurant  is success: "+restaurantId);
		return ResponseEntity.ok(userList);
	}
}
