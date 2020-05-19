package com.food.table.controller;

import com.food.table.model.FoodCategoriesModel;
import com.food.table.service.FoodCategoriesService;
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
	
	@GetMapping("/food_categories")
	public ResponseEntity<List<FoodCategoriesModel>> getFoodCategories() {
		List<FoodCategoriesModel> foodCategoriesResponse= foodCategoriesService.getFoodCategories();
		return ResponseEntity.ok(foodCategoriesResponse);
	}
	
	@PostMapping("/food_categories")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<FoodCategoriesModel> addNewDiets(@RequestBody FoodCategoriesModel foodCategory) {
		FoodCategoriesModel foodCategoriesResponse = foodCategoriesService.addNewFoodCategories(foodCategory);
		return ResponseEntity.ok(foodCategoriesResponse);
	}
	
	@GetMapping("/food_categories/{foodCategoryId}")
	public ResponseEntity<FoodCategoriesModel> getDietById(@NotNull @PathVariable int foodCategoryId) {
		FoodCategoriesModel foodCategoriesResponse = foodCategoriesService.getFoodCategoryById(foodCategoryId);
		return ResponseEntity.ok(foodCategoriesResponse);
	}
	
	@PutMapping("/food_categories/{foodCategoryId}")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<FoodCategoriesModel> updateDietById(@NotNull @PathVariable int foodCategoryId,
			@RequestBody FoodCategoriesModel foodCategoryRequest) {
		FoodCategoriesModel foodCategoriesResponse = foodCategoriesService.updateFoodCategoryById(foodCategoryId, foodCategoryRequest);
		return ResponseEntity.ok(foodCategoriesResponse);
	}	
	
}
