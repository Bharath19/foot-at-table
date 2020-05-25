package com.food.table.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.food.table.model.FavoriteRestaurantModel;

public interface UserService {
	void addFavoriteRestaurant(int restaurantId);

	void deleteFavoriteRestaurant(int restaurantId);

	List<FavoriteRestaurantModel> getAllFavoriteRestaurant(String latitude, String longitude, int from, int limit);
}
