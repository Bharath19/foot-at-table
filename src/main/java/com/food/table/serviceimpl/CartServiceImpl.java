package com.food.table.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.constant.CartOrderStatus;
import com.food.table.dto.Cart;
import com.food.table.dto.Foods;
import com.food.table.model.FoodResponseModel;
import com.food.table.repo.CartRepository;
import com.food.table.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepository cartRepository;

	@Override
	public ArrayList<FoodResponseModel> getFoodHistoryByRestaurantId(int restaurantId, Date orderDate) {

		ArrayList<Cart> carts = cartRepository.findByOrderStatusAndUpdatedAt(CartOrderStatus.SUCCESS.toString(),
				new SimpleDateFormat("yyyy-MM-dd").format(orderDate).toString());

		ArrayList<FoodResponseModel> foodResponse = new ArrayList<>();
		carts.forEach(cart -> {
			foodResponse.add(CartServiceImpl.convertToFoodResponse(cart));
		});

		return foodResponse;
	}

	private static FoodResponseModel convertToFoodResponse(Cart cart) {
		if (cart == null)
			return null;
		Foods food = cart.getFood();
		FoodResponseModel foodResponseModel = FoodResponseModel.builder().id(food.getId()).name(food.getName())
				.description(food.getDescription()).imageUrl(food.getImageUrl()).price(food.getPrice()).build();

		return foodResponseModel;
	}

}
