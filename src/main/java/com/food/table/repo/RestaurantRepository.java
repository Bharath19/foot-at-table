package com.food.table.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	
	Page<Restaurant> findByState(String state,Pageable pageable);
	
	@Query(value="SELECT * FROM fooddb.restaurant_detail r where r.state=:state and r.restaurant_name like %:restaurantName%",nativeQuery = true)
	Page<Restaurant> findByName(@Param("state") String state,@Param("restaurantName") String restaurantName,Pageable pageable);
	
	@Query(value="SELECT res.* FROM restaurant_detail res JOIN(SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM address_detail ad) AS T ON res.address_id = T.id  where res.state=:state and T.distance < :km ORDER BY T.distance",nativeQuery = true)
	Page<Restaurant> findByDistanceFilter(@Param("state") String state,@Param("latitude") String latitude, @Param("longitude") String longitude,
			@Param("km") String km,Pageable pageable);	
	
	@Query(value = "SELECT res.* FROM restaurant_detail res JOIN(SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM address_detail ad) AS T ON res.address_id = T.id  where res.state=:state ORDER BY T.distance", nativeQuery = true)
	Page<Restaurant> findByStateWithDistance(@Param("state") String state, @Param("latitude") String latitude,
			@Param("longitude") String longitude, Pageable pageable);
	
	@Query(value = "SELECT * FROM restaurant_detail res JOIN(SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM address_detail ad ) AS T ON res.address_id = T.id LEFT JOIN restaurant_services ser ON res.id=ser.restaurant_id LEFT JOIN restaurant_cuisines cus ON res.id=cus.restaurant_id LEFT JOIN restaurant_seatings sea ON res.id=sea.restaurant_id LEFT JOIN restaurant_diet die ON res.id=die.restaurant_id LEFT JOIN restaurant_types typ ON res.id=typ.restaurant_id WHERE res.state=:state AND ser.services_id IN (SELECT s.id FROM services s WHERE s.name IN :restaurantService ) AND seatings_id IN (SELECT s.id FROM seatings s WHERE s.name IN :restaurantSeating ) AND cus.cuisines_id IN (SELECT s.id FROM cuisines s WHERE s.name IN :restaurantCuisine ) AND typ.types_id IN (SELECT s.id FROM types s WHERE s.name IN :restaurantType ) AND die.diet_id IN (SELECT s.id FROM diets s WHERE s.name IN :restaurantDiet ) GROUP BY res.id ORDER BY T.distance", nativeQuery = true)
	Page<Restaurant> findBySearchWithLocation(@Param("state") String state, @Param("latitude") String latitude,@Param("longitude") String longitude,
			@Param("restaurantService") List<String> restaurantService,
			@Param("restaurantSeating") List<String> restaurantSeating,
			@Param("restaurantCuisine") List<String> restaurantCuisine,
			@Param("restaurantType") List<String> restaurantType, @Param("restaurantDiet") List<String> restaurantDiet,
			 Pageable pageable);
	
	@Query(value = "SELECT * FROM restaurant_detail res JOIN(SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM address_detail ad HAVING distance < :km) AS T ON res.address_id = T.id LEFT JOIN restaurant_services ser ON res.id=ser.restaurant_id LEFT JOIN restaurant_cuisines cus ON res.id=cus.restaurant_id LEFT JOIN restaurant_seatings sea ON res.id=sea.restaurant_id LEFT JOIN restaurant_diet die ON res.id=die.restaurant_id LEFT JOIN restaurant_types typ ON res.id=typ.restaurant_id WHERE res.state=:state AND ser.services_id IN (SELECT s.id FROM services s WHERE s.name IN :restaurantService ) AND seatings_id IN (SELECT s.id FROM seatings s WHERE s.name IN :restaurantSeating ) AND cus.cuisines_id IN (SELECT s.id FROM cuisines s WHERE s.name IN :restaurantCuisine ) AND typ.types_id IN (SELECT s.id FROM types s WHERE s.name IN :restaurantType ) AND die.diet_id IN (SELECT s.id FROM diets s WHERE s.name IN :restaurantDiet ) GROUP BY res.id ORDER BY T.distance", nativeQuery = true)
	Page<Restaurant> findBySearchWithKm(@Param("state") String state, @Param("latitude") String latitude,@Param("longitude") String longitude,
			@Param("restaurantService") List<String> restaurantService,
			@Param("restaurantSeating") List<String> restaurantSeating,
			@Param("restaurantCuisine") List<String> restaurantCuisine,
			@Param("restaurantType") List<String> restaurantType, @Param("restaurantDiet") List<String> restaurantDiet,@Param("km") String km,
			 Pageable pageable);	

}
