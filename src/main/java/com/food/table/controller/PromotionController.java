package com.food.table.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.FoodsModel;
import com.food.table.model.PromotionFoodModel;
import com.food.table.model.PromotionRestaurantModel;
import com.food.table.model.RestaurantGetModel;
import com.food.table.service.PromotionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/promotion")
@Api(value="Restaurant & Food Promotion Management System")
public class PromotionController {

	@Autowired
	PromotionService promotionService;
	
	@ApiOperation(value = "Subscribe restaurant promotion", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/restaurant")
	public ResponseEntity<Void> addRestaurantPromotion(@Valid @RequestBody PromotionRestaurantModel promotionModel) {
		promotionService.addRestaurantPromotionService(promotionModel);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Get list of restaurants promoted in the particular area", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/restaurant")
	public ResponseEntity<List<RestaurantGetModel>> getPromotionsRestaurant(
			@RequestParam(value = "latitude") String latitude, @RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "km", required = false, defaultValue = "10") String km) {
		List<RestaurantGetModel> restaurantModel = promotionService.getRestaurantPromotions(latitude, longitude, km);
		return ResponseEntity.ok(restaurantModel);
	}
	
	@ApiOperation(value = "Unsubcribe restaurant promotion", authorizations = {@Authorization(value = "accessToken")})
	@DeleteMapping("/restaurant")
	public ResponseEntity<Void> deleteRestaurantPromotion(@RequestParam(value = "restaurantId") Integer restaurantId) {
		promotionService.deleteRestaurantPromotionService(restaurantId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "Add and edit food promotion for particular restaurant", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/food")
	public ResponseEntity<Void> addFoodPromotion(@Valid @RequestBody PromotionFoodModel promotionFoodModel) {
		if (promotionFoodModel.getFoods().size() <= 0 || promotionFoodModel.getFoods().size() > 5) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.FOOD_PROMOTION_SIZE_INVALID);
		}
		promotionService.addUpdateFoodPromotionService(promotionFoodModel);
		;
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "View a list of available promoted food for particular restaurant", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/food")
	public ResponseEntity<List<FoodsModel>> getFoodPromotion(
			@RequestParam(value = "restaurantId") Integer restaurantId) {
		List<FoodsModel> foodsModel = promotionService.getFoodPromotionService(restaurantId);
		return ResponseEntity.ok(foodsModel);
	}
	
	@ApiOperation(value = "Delete food promotion for particular restaurant", authorizations = {@Authorization(value = "accessToken")})
	@DeleteMapping("/food")
	public ResponseEntity<Void> deleteFoodPromotion(@RequestParam(value = "restaurantId") Integer restaurantId) {
		promotionService.deleteRestaurantPromotionService(restaurantId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
