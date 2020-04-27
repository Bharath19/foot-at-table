package com.food.table.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.dto.FoodCategory;
import com.food.table.model.FoodCategoriesModel;
import com.food.table.repo.FoodCategoryRepository;
import com.food.table.service.FoodCategoriesService;

@Service
public class FoodCategoriesServiceImpl implements FoodCategoriesService{
	
	@Autowired
	FoodCategoryRepository foodCategoryRepository;

	@Override
	public List<FoodCategoriesModel> getFoodCategories() {
		List<FoodCategoriesModel> foodCategoryResponse = new ArrayList<FoodCategoriesModel>();
		List<FoodCategory> foodCategories = foodCategoryRepository.findAll();
		foodCategories.forEach(data -> {
			FoodCategoriesModel foodCategory = FoodCategoriesModel.builder().id(data.getId()).name(data.getName()).build();
			foodCategoryResponse.add(foodCategory);
		});

		return foodCategoryResponse;
	}

	@Override
	public FoodCategoriesModel addNewFoodCategories(FoodCategoriesModel newFoodCategory) {
		FoodCategory foodCategory = FoodCategory.builder().name(newFoodCategory.getName()).description(newFoodCategory.getDescription()).build();
		FoodCategory foodCategoryInsert=foodCategoryRepository.save(foodCategory);
		newFoodCategory.setId(foodCategoryInsert.getId());
		return newFoodCategory;
	}

	@Override
	public FoodCategoriesModel getFoodCategoryById(int foodCategoryId) {
		Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(foodCategoryId);
		FoodCategoriesModel foodCategoryResponse = FoodCategoriesModel.builder().id(foodCategory.get().getId()).name(foodCategory.get().getName())
																				.description(foodCategory.get().getDescription()).build();
		return foodCategoryResponse;
	}

	@Override
	public FoodCategoriesModel updateFoodCategoryById(int foodCategoryId, FoodCategoriesModel foodCategoryRequest) {
		Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(foodCategoryId);
		foodCategory.get().setName(foodCategoryRequest.getName());
		FoodCategory foodCategoryUpdate=foodCategoryRepository.save(foodCategory.get());
		return FoodCategoriesModel.builder().id(foodCategoryUpdate.getId()).name(foodCategoryUpdate.getName())
											.description(foodCategoryUpdate.getDescription()).build();

	}

}
