package com.food.table.repo;

import com.food.table.dto.RestaurantEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantEmployeeRepository extends JpaRepository<RestaurantEmployee, Integer> {

    @Query(value = "SELECT * FROM restaurant_employee f where f.delete_flag=0 and f.restaurant_id=:id", nativeQuery = true)
    List<RestaurantEmployee> findEmployeeByRestaurantId(@Param("id") int id);

    @Query(value = "SELECT * FROM restaurant_employee f where f.delete_flag=0 and f.id=:id", nativeQuery = true)
    RestaurantEmployee findEmployeeById(@Param("id") int id);
}
