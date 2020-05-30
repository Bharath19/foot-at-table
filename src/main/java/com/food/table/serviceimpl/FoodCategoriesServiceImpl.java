package com.food.table.serviceimpl;

import com.food.table.dto.FoodCategory;
import com.food.table.dto.Restaurant;
import com.food.table.dto.UserAccount;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.FoodCategoriesModel;
import com.food.table.repo.FoodCategoryRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.service.FoodCategoriesService;
import com.food.table.util.AuthorityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FoodCategoriesServiceImpl implements FoodCategoriesService{
	
	@Autowired
	FoodCategoryRepository foodCategoryRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	AuthorityUtil authorityUtil;

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
		Optional<Restaurant> restaurant = restaurantRepository.findById(newFoodCategory.getRestaurantId());
		if (!restaurant.isPresent())
			throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
		checkAuthority(newFoodCategory.getRestaurantId());
		FoodCategory foodCategory = FoodCategory.builder()
				.name(newFoodCategory.getName())
				.restaurant(restaurant.get())
				.description(newFoodCategory.getDescription())
				.sortOrder(newFoodCategory.getSortOrder()).build();
		FoodCategory foodCategoryInsert=foodCategoryRepository.save(foodCategory);
		newFoodCategory.setId(foodCategoryInsert.getId());
		return newFoodCategory;
	}

	@Override
	public FoodCategoriesModel getFoodCategoryById(int foodCategoryId) {
		Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(foodCategoryId);
		Optional<Restaurant> restaurant = restaurantRepository.findById(foodCategory.get().getRestaurant().getId());
		if (!restaurant.isPresent())
			throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
		checkAuthority(foodCategory.get().getRestaurant().getId());
		if (!foodCategory.isPresent())
			throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_FOOD_CATEGORY);
		FoodCategoriesModel foodCategoryResponse = FoodCategoriesModel.builder().id(foodCategory.get().getId()).name(foodCategory.get().getName())
																				.description(foodCategory.get().getDescription()).build();
		return foodCategoryResponse;
	}

	@Override
	public FoodCategoriesModel updateFoodCategoryById(int foodCategoryId, FoodCategoriesModel foodCategoryRequest) {
		Optional<Restaurant> restaurant = restaurantRepository.findById(foodCategoryRequest.getRestaurantId());
		if (!restaurant.isPresent())
			throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
		checkAuthority(foodCategoryRequest.getRestaurantId());
		Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(foodCategoryId);
		if (!foodCategory.isPresent())
			throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_FOOD_CATEGORY);
		checkAuthority(foodCategory.get().getRestaurant().getId());
		foodCategory.get().setName(foodCategoryRequest.getName());
		foodCategory.get().setDescription(foodCategoryRequest.getDescription());
		foodCategory.get().setSortOrder(foodCategoryRequest.getSortOrder());
		foodCategory.get().setRestaurant(restaurant.get());
		FoodCategory foodCategoryUpdate=foodCategoryRepository.save(foodCategory.get());
		return FoodCategoriesModel.builder().id(foodCategoryUpdate.getId()).name(foodCategoryUpdate.getName())
				.description(foodCategoryUpdate.getDescription())
				.sortOrder(foodCategoryUpdate.getSortOrder())
				.restaurantId(foodCategoryUpdate.getRestaurant().getId())
				.build();

	}

	private void checkAuthority(int restaurantId) {
		UserAccount userDetails = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		authorityUtil.checkAuthority(userDetails, restaurantId);
	}

}
