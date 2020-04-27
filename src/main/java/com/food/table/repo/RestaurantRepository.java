package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>{
	
	@Query(value="SELECT * FROM fooddb.restaurant_detail r where r.state=:state",nativeQuery = true)
	List<Restaurant> findByState(@Param("state") String state);
	

}
