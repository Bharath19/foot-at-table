package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.RestaurantFeedback;

@Repository
public interface FeedbackRepository extends JpaRepository<RestaurantFeedback, Long>{

}
