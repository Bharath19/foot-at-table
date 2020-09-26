package com.food.table.service;

import java.util.List;

import com.food.table.model.FeedbackModel;

public interface FeedbackService {

	public void addRestaurantFeedback(int restaurantId, FeedbackModel feedbackModel);

	public List<FeedbackModel> getRestaurantFeedbackByUser(int restaurantId, int userId);
}
