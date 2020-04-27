package com.food.table.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.FoodCategoriesModel;
import com.food.table.service.FoodCategoriesService;

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
	public ResponseEntity<FoodCategoriesModel> updateDietById(@NotNull @PathVariable int foodCategoryId,
			@RequestBody FoodCategoriesModel foodCategoryRequest) {
		FoodCategoriesModel foodCategoriesResponse = foodCategoriesService.updateFoodCategoryById(foodCategoryId, foodCategoryRequest);
		return ResponseEntity.ok(foodCategoriesResponse);
	}	
	
}
