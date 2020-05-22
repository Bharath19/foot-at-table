package com.food.table.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.food.table.model.FoodsModel;
import com.food.table.model.PromotionFoodModel;
import com.food.table.model.PromotionRestaurantModel;
import com.food.table.model.RestaurantGetModel;

@Service
public interface PromotionService {

	public void addRestaurantPromotionService(PromotionRestaurantModel promotionModel);

	public List<RestaurantGetModel> getRestaurantPromotions(String latitude, String longitude, String km);

	public void deleteRestaurantPromotionService(Integer restaurantId);

	public void addUpdateFoodPromotionService(PromotionFoodModel promotionFoodModel);

	public List<FoodsModel> getFoodPromotionService(Integer restaurantId);

	public void deleteFoodPromotionService(Integer restaurantId);
}
