package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.PromotionFood;
import com.food.table.dto.Restaurant;

@Repository
public interface PromotionFoodRepository extends JpaRepository<PromotionFood, Long> {

	List<PromotionFood> findByRestaurant(Restaurant restaurant);

}
