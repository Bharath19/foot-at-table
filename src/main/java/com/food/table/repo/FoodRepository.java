package com.food.table.repo;

import com.food.table.dto.Foods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Foods, Integer> {

    @Query(value = "SELECT * FROM foods f where f.delete_flag=0", nativeQuery = true)
    List<Foods> findAllFood();

    @Query(value = "SELECT * FROM foods f where f.delete_flag=0 and f.id=:id", nativeQuery = true)
    Foods findFoodById(@Param("id") int id);

    @Query(value = "SELECT * FROM foods f where f.delete_flag=0 and f.restaurant_id=:id", nativeQuery = true)
    List<Foods> findFoodsByRestaurantId(@Param("id") int id);
}
