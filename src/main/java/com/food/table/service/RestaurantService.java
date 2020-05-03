package com.food.table.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.food.table.model.RestaurantGetModel;
import com.food.table.model.RestaurantModel;

@Service
public interface RestaurantService {
	
	public void addRestaurant(RestaurantModel restaurantModel);
	
	public boolean deleteRestaurant(int id);
	
	public List<RestaurantGetModel> getAllRestaurant(int from,int limit,String latitude,String longtitude);
	
	public void updateRestaurant(RestaurantModel restaurantModel);
	
	public List<RestaurantGetModel> getRestaurantByDietType(List<String> dietType,int from,int limit,String latitude,String longtitude);
	
	public List<RestaurantGetModel> getRestaurantByRestaurantType(List<String> restaurantType,int from,int limit,String latitude,String longtitude);
	
	public List<RestaurantGetModel> getRestaurantByRestaurantSeating(List<String> restaurantSeating,int from,int limit,String latitude,String longtitude);
	
	public List<RestaurantGetModel> getRestaurantByRestaurantService(List<String> restaurantService,int from,int limit,String latitude,String longtitude);
	
	public List<RestaurantGetModel> getRestaurantByRestaurantCuisine(List<String> restaurantCuisine,int from,int limit,String latitude,String longtitude);
	
	public List<RestaurantGetModel> getRestaurantByRestaurantName(String restaurantName,int from,int limit,String latitude,String longtitude);
	
	public List<RestaurantGetModel> getRestaurantByDistance(String latitude,String longitude,String km);
}
