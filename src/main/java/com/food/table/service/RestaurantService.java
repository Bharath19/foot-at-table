package com.food.table.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.food.table.model.DefaultValuesResponse;
import com.food.table.model.RestaurantGetModel;
import com.food.table.model.RestaurantModel;
import com.food.table.model.RestaurantUpdateRequest;
import com.food.table.model.SearchModel;
import com.food.table.model.TimingModel;

@Service
public interface RestaurantService {
	
	public void addRestaurant(RestaurantModel restaurantModel);
	
	public void deleteRestaurant(int id);
	
	public List<RestaurantGetModel> getAllRestaurant(int from,int limit,String latitude,String longtitude,String km);
	
	public void updateRestaurant(RestaurantModel restaurantModel);	
	
	public List<RestaurantGetModel> getRestaurantByRestaurantName(String restaurantName,int from,int limit,String latitude,String longtitude);	
	
	public List<RestaurantGetModel> getRestaurantByFilter(String latitude, String longitude, int from, int limit,
			String km, List<String> restaurantService, List<String> restaurantSeating, List<String> restaurantCuisine,
			List<String> restaurantType, List<String> restaurantDiet);

	public List<RestaurantGetModel> getAllDraftedRestaurant(int from, int limit);
	
	public DefaultValuesResponse getDefaultTableValues() ;

	public void updateStateAndStatus(int id, RestaurantUpdateRequest restaurantUpdateRequest);

	public List<TimingModel> getRestaurantTimings(int restaurantId);
}
