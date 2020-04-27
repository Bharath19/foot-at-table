package com.food.table.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.food.table.model.FoodCategoriesModel;

@Service
public interface FoodCategoriesService{

	List<FoodCategoriesModel> getFoodCategories();
	
	FoodCategoriesModel addNewFoodCategories(FoodCategoriesModel foodCategory);
	
	FoodCategoriesModel getFoodCategoryById(int foodCategoryId);
	
	FoodCategoriesModel updateFoodCategoryById(int foodCategoryId, FoodCategoriesModel foodCategoryRequest);
}
