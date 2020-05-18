package com.food.table.service;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.food.table.model.FoodResponseModel;

@Service
public interface CartService {

	ArrayList<FoodResponseModel> getFoodHistoryByRestaurantId(int restaurantId, Date date);

}
