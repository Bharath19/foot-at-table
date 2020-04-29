package com.food.table.repo;

import com.food.table.dto.FoodTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodTagRepository extends JpaRepository<FoodTag, Integer> {
}
