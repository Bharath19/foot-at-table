package com.food.table.serviceimpl;

import com.food.table.dto.Order;
import com.food.table.dto.Restaurant;
import com.food.table.dto.RestaurantFeedback;
import com.food.table.dto.UserAccount;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.FeedbackModel;
import com.food.table.repo.FeedbackRepository;
import com.food.table.repo.OrderRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.UserRepository;
import com.food.table.service.FeedbackService;
import com.food.table.util.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Autowired
	private UserUtil userUtil;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void addRestaurantFeedback(int restaurantId, FeedbackModel feedbackModel) {
		RestaurantFeedback restaurantFeedback = parseRestauarantFeedbackDTO(restaurantId, feedbackModel);
		RestaurantFeedback response = feedbackRepository.save(restaurantFeedback);
		if (response == null) {
			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.ADD_FEEDBACK_FAILED);
		}
		boolean check = updateRestauarntRating(feedbackModel, restaurantId);
		if (!check) {
			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.ADD_FEEDBACK_FAILED);
		}
	}

	private RestaurantFeedback parseRestauarantFeedbackDTO(int restaurantId, FeedbackModel feedbackModel) {
		RestaurantFeedback restaurantFeedback = new RestaurantFeedback();
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		if (restaurant.isPresent()) {
			restaurantFeedback.setRestaurant(restaurant.get());
			restaurantRepository.save(restaurant.get());
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}
		Optional<Order> order = orderRepository.findById(feedbackModel.getOrderId());
		if (order.isPresent()) {
			restaurantFeedback.setOrder(order.get());
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_ID);
		}
		UserAccount user = userUtil.getCurrentUserId();
		if (user!=null) {
			restaurantFeedback.setUser(user);
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_USER_ID);
		}
		restaurantFeedback.setMessage(feedbackModel.getMessage());
		restaurantFeedback.setRating(feedbackModel.getRating());
		return restaurantFeedback;
	}
	
	@Caching(evict = {
			@CacheEvict(cacheNames = "allDraftedRestaurant", allEntries = true),
			@CacheEvict(cacheNames = "allConfirmedRestaurant", allEntries = true),
			@CacheEvict(cacheNames = "getRestaurantTimings", allEntries = true),
			@CacheEvict(cacheNames = "getRestaurantById", allEntries = true),
			@CacheEvict(cacheNames = "restaurantByName", allEntries = true)
	})
	private boolean updateRestauarntRating(FeedbackModel feedbackModel, int restaurantId) {
		boolean check = false;
		Optional<Restaurant> restaurantOp = restaurantRepository.findById(restaurantId);
		Restaurant restaurant = restaurantOp.get();
		double newRating = feedbackModel.getRating();
		double oldRating = restaurant.getRating();
		int totalCount = restaurant.getRatingCount() + 1;
		double value = oldRating + ((newRating - oldRating) / totalCount);
		restaurant.setRating(value);
		restaurant.setRatingCount(totalCount);
		Restaurant saveResponse = restaurantRepository.save(restaurant);
		if (saveResponse != null) {
			check = true;
		}
		return check;
	}

	@Override
	public List<FeedbackModel> getRestaurantFeedbackByUser(int restaurantId, int userId) {
		List<FeedbackModel> feedbackModel = new ArrayList<FeedbackModel>();
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		if(restaurant.isPresent()) {
			Optional<UserAccount> userAccount = userRepository.findById(userId);
			if(userAccount.isPresent()) {
				List<RestaurantFeedback> restaurantFeedback = feedbackRepository.getRestaurantFeedBack(restaurantId,userId);
				if(restaurantFeedback!=null && !restaurantFeedback.isEmpty()) {
					feedbackModel = parseRestaurantFeedback(restaurantFeedback);
				}
			} else  {
				throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_USER_ID);
			}
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}
		return feedbackModel;
	}
	
	private List<FeedbackModel> parseRestaurantFeedback(List<RestaurantFeedback> restaurantFeedback) {
		return restaurantFeedback.stream().map(val -> {
			FeedbackModel feed = new FeedbackModel();
			feed.setOrderId(val.getOrder().getId());
			feed.setMessage(val.getMessage());
			feed.setRating(val.getRating());
			return feed;
		}).collect(Collectors.toList());
	}

}
