package com.food.table.serviceimpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.constant.CartOrderStatus;
import com.food.table.model.FoodHistoryProjectionModel;
import com.food.table.repo.CartRepository;
import com.food.table.service.CartService;
import com.food.table.util.SimpleDateUtil;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepository cartRepository;

	@Override
	public List<FoodHistoryProjectionModel> getFoodHistoryByRestaurantId(int restaurantId, Date orderDate) {

		return cartRepository.findByOrderStatusAndUpdatedAt(CartOrderStatus.SUCCESS.toString(),
				SimpleDateUtil.toDate(orderDate));

	}

}
