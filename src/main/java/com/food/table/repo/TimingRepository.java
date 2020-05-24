package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.table.dto.Restaurant;
import com.food.table.dto.Timings;

public interface TimingRepository extends JpaRepository<Timings, Integer> {
	
	
	List<Timings> findByRestaurantId(Integer restaurantId);

}
