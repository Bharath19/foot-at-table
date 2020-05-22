package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.table.dto.PromotionRestaurant;
import com.food.table.dto.Restaurant;

public interface PromotionRestaurantRepository extends JpaRepository<PromotionRestaurant, Long> {

	PromotionRestaurant findByRestaurant(Restaurant restaurant);

}
