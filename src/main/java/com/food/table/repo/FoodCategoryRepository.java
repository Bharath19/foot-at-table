package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.food.table.dto.FoodCategory;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Integer>{

}
