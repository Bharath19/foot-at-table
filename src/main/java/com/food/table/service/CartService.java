package com.food.table.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.food.table.model.FoodHistoryProjectionModel;

@Service
public interface CartService {

	List<FoodHistoryProjectionModel> getFoodHistoryByRestaurantId(int restaurantId, Date date);

}
