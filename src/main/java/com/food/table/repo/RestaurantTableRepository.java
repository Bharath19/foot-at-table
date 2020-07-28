package com.food.table.repo;

import com.food.table.dto.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {

    List<RestaurantTable> findByRestaurantIdAndNameContainingIgnoreCase(int id,String name);

    RestaurantTable findById(int id);


    List<RestaurantTable> findAll();

    RestaurantTable findByQrCode(String qrCode);

    RestaurantTable findByNameAndRestaurantId(String qrCode,int restaurantId);

	Optional<RestaurantTable> findByIdAndRestaurantId(int restaurantTableId, int restaurantId);
}
