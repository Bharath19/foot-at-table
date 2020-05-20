package com.food.table.controller;

import com.food.table.model.FoodCategoriesModel;
import com.food.table.service.FoodCategoriesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class FoodCategoriesController {

	@Autowired
	FoodCategoriesService foodCategoriesService;

	@ApiOperation(value = "View list of all Food Categories", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/food_categories")
	public ResponseEntity<List<FoodCategoriesModel>> getFoodCategories() {
		List<FoodCategoriesModel> foodCategoriesResponse= foodCategoriesService.getFoodCategories();
		return ResponseEntity.ok(foodCategoriesResponse);
	}

	@ApiOperation(value = "Add a new Food Category", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/food_categories")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<FoodCategoriesModel> addNewFoodCategory(@RequestBody FoodCategoriesModel foodCategory) {
		FoodCategoriesModel foodCategoriesResponse = foodCategoriesService.addNewFoodCategories(foodCategory);
		return ResponseEntity.ok(foodCategoriesResponse);
	}

	@ApiOperation(value = "Get Food Category by Id", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/food_categories/{foodCategoryId}")
	public ResponseEntity<FoodCategoriesModel> getFoodCategoryById(@NotNull @PathVariable int foodCategoryId) {
		FoodCategoriesModel foodCategoriesResponse = foodCategoriesService.getFoodCategoryById(foodCategoryId);
		return ResponseEntity.ok(foodCategoriesResponse);
	}
	
	@PutMapping("/food_categories/{foodCategoryId}")
	@ApiOperation(value = "Update a Food Category", authorizations = {@Authorization(value = "accessToken")})
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<FoodCategoriesModel> updateFoodCategoryById(@NotNull @PathVariable int foodCategoryId,
																	  @RequestBody FoodCategoriesModel foodCategoryRequest) {
		FoodCategoriesModel foodCategoriesResponse = foodCategoriesService.updateFoodCategoryById(foodCategoryId, foodCategoryRequest);
		return ResponseEntity.ok(foodCategoriesResponse);
	}	
	
}
