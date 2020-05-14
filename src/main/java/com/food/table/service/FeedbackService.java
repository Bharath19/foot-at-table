package com.food.table.service;

import com.food.table.model.FeedbackModel;

public interface FeedbackService {

	public void addRestaurantFeedback(int restaurantId,FeedbackModel feedbackModel);
}
