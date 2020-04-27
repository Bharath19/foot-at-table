package com.food.table.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.food.table.model.RestaurantGetModel;
import com.food.table.model.RestaurantModel;

@Service
public interface RestaurantService {
	
	public void addRestaurant(RestaurantModel restaurantModel);
	
	public boolean deleteRestaurant(int id);
	
	public List<RestaurantGetModel> getAllRestaurant();
	
	public void updateRestaurant(RestaurantModel restaurantModel);
}
