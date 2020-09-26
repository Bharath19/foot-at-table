package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.food.table.dto.RestaurantFeedback;

@Repository
public interface FeedbackRepository extends JpaRepository<RestaurantFeedback, Long> {

	@Query(value = "SELECT * FROM restaurant_feedback where restaurant_id=:restaurantId and user_id=:userId", nativeQuery = true)
	List<RestaurantFeedback> getRestaurantFeedBack(@Param("restaurantId") Integer restaurantId,
			@Param("userId") Integer userId);

}
