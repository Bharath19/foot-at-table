package com.food.table.repo;

import com.food.table.dto.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Integer>{

    @Query(value = "SELECT * FROM skipthequeue.food_categories f where f.restaurant_id=:id", nativeQuery = true)
    List<FoodCategory> findFoodCategoriesByRestaurantId(@Param("id") int id);

}
