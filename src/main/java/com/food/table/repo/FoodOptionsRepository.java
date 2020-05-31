package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.food.table.dto.FoodOptions;

public interface FoodOptionsRepository extends JpaRepository<FoodOptions, Integer>{

	@Query(value = "SELECT fo.* FROM food_options fo INNER JOIN food_options_map fomp on fo.id = fomp.food_options_id INNER JOIN food_option_meta fome ON fomp.food_option_meta_id = fome.id where fo.id = :id and fome.foods_id = :foodId", nativeQuery = true)
	FoodOptions findIdByIdAndFoodId(int id, int foodId);
}
