package com.food.table.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>{
	
	@Query(value="SELECT * FROM fooddb.restaurant_detail r where r.state=:state",nativeQuery = true)
	Page<Restaurant> findByState(@Param("state") String state,Pageable pageable);
	
	@Query(value="select * from `fooddb`.`restaurant_detail` res left join `fooddb`.`restaurant_diet` die on res.id=die.restaurant_id where  res.state=:state and die.diet_id IN (select d.id from `fooddb`.`diets` d where d.name in :dietType) group by res.id",nativeQuery = true)
	Page<Restaurant> findByDiets(@Param("state") String state,@Param("dietType") List<String> dietType,Pageable pageable);
	
	@Query(value="select * from `fooddb`.`restaurant_detail` res left join `fooddb`.`restaurant_types` typ on res.id=typ.restaurant_id where  res.state=:state and typ.types_id IN (select t.id from `fooddb`.`types` t where t.name in :restaurantType) group by res.id",nativeQuery = true)
	Page<Restaurant> findByRestauratTypes(@Param("state") String state,@Param("restaurantType") List<String> restaurantType,Pageable pageable);
	
	@Query(value="SELECT * FROM fooddb.restaurant_detail r where r.state=:state and r.restaurant_name like %:restaurantName%",nativeQuery = true)
	Page<Restaurant> findByName(@Param("state") String state,@Param("restaurantName") String restaurantName,Pageable pageable);
	
	@Query(value="SELECT res.* FROM `fooddb`.`restaurant_detail` res JOIN(SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM `fooddb`.address_detail ad) AS T ON res.address_id = T.id  where res.state=:state and T.distance < :km ORDER BY T.distance",nativeQuery = true)
	List<Restaurant> findByDistance(@Param("state") String state,@Param("latitude") String latitude, @Param("longitude") String longitude,
			@Param("km") String km);
	
	@Query(value="select * from `fooddb`.`restaurant_detail` res left join `fooddb`.`restaurant_seatings` sea on res.id=sea.restaurant_id where  res.state=:state and sea.seatings_id IN (select s.id from `fooddb`.`seatings` s where s.name in :restaurantSeating) group by res.id",nativeQuery = true)
	Page<Restaurant> findByRestauratSeatings(@Param("state") String state,@Param("restaurantSeating") List<String> restaurantSeating,Pageable pageable);
	
	@Query(value="select * from `fooddb`.`restaurant_detail` res left join `fooddb`.`restaurant_cuisines` cus on res.id=cus.restaurant_id where  res.state=:state and cus.cuisines_id IN (select c.id from `fooddb`.`cuisines` c where c.name in :restaurantCuisine) group by res.id",nativeQuery = true)
	Page<Restaurant> findByRestauratCuisines(@Param("state") String state,@Param("restaurantCuisine") List<String> restaurantCuisine,Pageable pageable);
	
	@Query(value="select * from `fooddb`.`restaurant_detail` res left join `fooddb`.`restaurant_services` ser on res.id=ser.restaurant_id where  res.state=:state and ser.services_id IN (select s.id from `fooddb`.`services` s where s.name in :restaurantService) group by res.id",nativeQuery = true)
	Page<Restaurant> findByRestauratServices(@Param("state") String state,@Param("restaurantService") List<String> restaurantService,Pageable pageable);
	
	@Query(value = "SELECT res.* FROM `fooddb`.`restaurant_detail` res JOIN( SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM `fooddb`.address_detail ad ) AS T ON res.address_id = T.id LEFT JOIN `fooddb`.`restaurant_services` ser ON res.id=ser.restaurant_id WHERE res.state= :state AND ser.services_id IN (SELECT s.id FROM `fooddb`.`services` s WHERE s.name IN :restaurantService) GROUP BY res.id ORDER BY T.distance\r\n"
			+ "", nativeQuery = true)
	Page<Restaurant> findByRestauratServicesWithDistance(@Param("state") String state,
			@Param("latitude") String latitude, @Param("longitude") String longitude,
			@Param("restaurantService") List<String> restaurantService, Pageable pageable);
	
	@Query(value = "SELECT res.* FROM `fooddb`.`restaurant_detail` res JOIN( SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM `fooddb`.address_detail ad ) AS T ON res.address_id = T.id LEFT JOIN `fooddb`.`restaurant_cuisines` cus ON res.id=cus.restaurant_id WHERE res.state= :state AND cus.cuisines_id IN (SELECT s.id FROM `fooddb`.`cuisines` s WHERE s.name IN :restaurantCuisine) GROUP BY res.id ORDER BY T.distance", nativeQuery = true)
	Page<Restaurant> findByRestauratCuisinesWithDistance(@Param("state") String state,
			@Param("latitude") String latitude, @Param("longitude") String longitude,
			@Param("restaurantCuisine") List<String> restaurantCuisine, Pageable pageable);
	
	@Query(value = "SELECT res.* FROM `fooddb`.`restaurant_detail` res JOIN( SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM `fooddb`.address_detail ad ) AS T ON res.address_id = T.id LEFT JOIN `fooddb`.`restaurant_seatings` sea ON res.id=sea.restaurant_id WHERE res.state= :state AND sea.seatings_id IN (SELECT s.id FROM `fooddb`.`seatings` s WHERE s.name IN :restaurantSeating) GROUP BY res.id ORDER BY T.distance\r\n"
			+ "", nativeQuery = true)
	Page<Restaurant> findByRestauratSeatingsWithDistance(@Param("state") String state,
			@Param("latitude") String latitude, @Param("longitude") String longitude,
			@Param("restaurantSeating") List<String> restaurantSeating, Pageable pageable);
	
	@Query(value = "SELECT res.* FROM `fooddb`.`restaurant_detail` res JOIN( SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM `fooddb`.address_detail ad ) AS T ON res.address_id = T.id LEFT JOIN `fooddb`.`restaurant_types` typ ON res.id=typ.restaurant_id WHERE res.state= :state AND typ.types_id IN (SELECT s.id FROM `fooddb`.`types` s WHERE s.name IN :restaurantType) GROUP BY res.id ORDER BY T.distance\r\n"
			+ "", nativeQuery = true)
	Page<Restaurant> findByRestauratTypesWithDistance(@Param("state") String state, @Param("latitude") String latitude,
			@Param("longitude") String longitude, @Param("restaurantType") List<String> restaurantType,
			Pageable pageable);
	
	@Query(value = "SELECT res.* FROM `fooddb`.`restaurant_detail` res JOIN( SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM `fooddb`.address_detail ad ) AS T ON res.address_id = T.id LEFT JOIN `fooddb`.`restaurant_diet` die ON res.id=die.restaurant_id WHERE res.state= :state AND die.diet_id IN (SELECT s.id FROM `fooddb`.`diets` s WHERE s.name IN :dietType) GROUP BY res.id ORDER BY T.distance\r\n"
			+ "", nativeQuery = true)
	Page<Restaurant> findByDietsWithDistance(@Param("state") String state, @Param("latitude") String latitude,
			@Param("longitude") String longitude, @Param("dietType") List<String> dietType, Pageable pageable);
	
	@Query(value = "SELECT res.* FROM `fooddb`.`restaurant_detail` res JOIN(SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM `fooddb`.address_detail ad) AS T ON res.address_id = T.id  where res.state=:state ORDER BY T.distance", nativeQuery = true)
	Page<Restaurant> findByStateWithDistance(@Param("state") String state, @Param("latitude") String latitude,
			@Param("longitude") String longitude, Pageable pageable);	
	
}
