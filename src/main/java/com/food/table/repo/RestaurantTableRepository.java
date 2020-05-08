package com.food.table.repo;

import com.food.table.dto.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {

    @Query(value = "SELECT * FROM restaurant_table f where f.delete_flag=0 and f.restaurant_id=:id", nativeQuery = true)
    List<RestaurantTable> findTablesByRestaurantId(@Param("id") int id);

    @Query(value = "SELECT * FROM restaurant_table f where f.delete_flag=0 and f.id=:id", nativeQuery = true)
    RestaurantTable findTableById(@Param("id") int id);

    @Query(value = "SELECT * FROM restaurant_table f where f.delete_flag=0", nativeQuery = true)
    List<RestaurantTable> findAllTables();

    @Query(value = "SELECT * FROM restaurant_table f where f.delete_flag=0 and f.qr_code=:qrCode", nativeQuery = true)
    RestaurantTable findTableByQRCode(@Param("qrCode") String qrCode);
}
