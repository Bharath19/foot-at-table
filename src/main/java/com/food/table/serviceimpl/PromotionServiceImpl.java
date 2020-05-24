package com.food.table.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.food.table.constant.ApplicationConstants;
import com.food.table.dto.Foods;
import com.food.table.dto.PromotionFood;
import com.food.table.dto.PromotionRestaurant;
import com.food.table.dto.Restaurant;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.FoodsModel;
import com.food.table.model.PromotionFoodModel;
import com.food.table.model.PromotionRestaurantModel;
import com.food.table.model.RestaurantGetModel;
import com.food.table.repo.FoodRepository;
import com.food.table.repo.PromotionFoodRepository;
import com.food.table.repo.PromotionRestaurantRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.service.PromotionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private PromotionRestaurantRepository promotionRestaurantRepository;

	@Autowired
	private FoodRepository foodRepository;

	@Autowired
	private PromotionFoodRepository promotionFoodRepository;

	@Autowired
	private RestaurantServiceImpl restaurantServiceImpl;

	@Override
	public void addRestaurantPromotionService(PromotionRestaurantModel promotionModel) {
		log.info("Entering add promotion for restaurant : " + promotionModel.getRestaurantId());

		Optional<Restaurant> restaurant = restaurantRepository.findById(promotionModel.getRestaurantId());
		if (restaurant.isPresent()) {
			PromotionRestaurant promotionRestaurant = promotionRestaurantRepository.findByRestaurant(restaurant.get());
			if (promotionRestaurant != null) {
				promotionRestaurant.setRankId(promotionModel.getRank());
			} else {
				promotionRestaurant = PromotionRestaurant.builder().restaurant(restaurant.get())
						.rankId(promotionModel.getRank()).build();
			}
			try {
				promotionRestaurantRepository.save(promotionRestaurant);
			} catch (ConstraintViolationException e) {
				log.error("Add Promotion for restaurant is failed by constraint violation : "
						+ promotionModel.getRestaurantId());
				throw new ApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR,
						ApplicationErrors.ADD_RESTAURANT_PROMOTION_FAILED);
			} catch (Exception e) {
				log.error("Add Promotion for restaurant is failed : " + promotionModel.getRestaurantId());
				throw new ApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR,
						ApplicationErrors.ADD_RESTAURANT_PROMOTION_FAILED);
			}

		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}

		log.info("Exiting add promotion for restaurant is success: " + promotionModel.getRestaurantId());
	}

	@Override
	public List<RestaurantGetModel> getRestaurantPromotions(String latitude, String longitude, String km) {
		log.info("Entering get promotion restaurant details");

		List<RestaurantGetModel> restaurantGetModels = new ArrayList<RestaurantGetModel>();
		String currentDay = LocalDate.now().getDayOfWeek().toString();
		List<Restaurant> restaurant = null;
		try {
			restaurant = restaurantRepository.findPromotionRestaurant(ApplicationConstants.confirmedState, latitude,
					longitude, km, currentDay);
		} catch (Exception e) {
			log.error("Get restaurant for promotion is failed");
			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationErrors.GET_RESTAURANT_PROMOTION_FAILED);
		}
		if (restaurant != null) {
			restaurantGetModels = restaurantServiceImpl.parseGetAllRestaurant(restaurant, latitude, longitude);
		}
		log.info("Exiting get promotion restaurant details is success");
		return restaurantGetModels;
	}

	@Override
	public void deleteRestaurantPromotionService(Integer restaurantId) {
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		if (restaurant.isPresent()) {
			PromotionRestaurant promotionRestaurant = promotionRestaurantRepository.findByRestaurant(restaurant.get());
			promotionRestaurantRepository.delete(promotionRestaurant);
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}

	}

	@Override
	public void addUpdateFoodPromotionService(PromotionFoodModel promotionFoodModel) {
		Optional<Restaurant> restaurant = restaurantRepository.findById(promotionFoodModel.getRestaurantId());
		if (restaurant.isPresent()) {
			List<PromotionFood> promotionFood = promotionFoodRepository.findByRestaurant(restaurant.get());
			if (!promotionFood.isEmpty() && promotionFood != null) {
				promotionFoodRepository.deleteAll(promotionFood);
			}
			promotionFood.clear();
			promotionFoodModel.getFoods().parallelStream().forEach(foodModel -> {
				Optional<Foods> food = foodRepository.findById(foodModel.getFooId());
				if (!food.isPresent()) {
					throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_ID);
				}
				promotionFood.add(PromotionFood.builder().restaurant(restaurant.get()).foods(food.get())
						.rankId(foodModel.getRankId()).build());
			});
			promotionFoodRepository.saveAll(promotionFood);
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}

	}

	@Override
	public void deleteFoodPromotionService(Integer restaurantId) {
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		if (restaurant.isPresent()) {
			List<PromotionFood> promotionFood = promotionFoodRepository.findByRestaurant(restaurant.get());
			if (!promotionFood.isEmpty() && promotionFood != null) {
				promotionFoodRepository.deleteAll(promotionFood);
			} else {
				throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.EMPTY_FOOD_PROMOTION);
			}
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}

	}

	@Override
	public List<FoodsModel> getFoodPromotionService(Integer restaurantId) {
		List<FoodsModel> foodList = new ArrayList<FoodsModel>();
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		if (restaurant.isPresent()) {
			List<Foods> foods = foodRepository.findPromotionFoods(restaurantId);
			if (!foods.isEmpty() && foods != null)
				foodList = foods.stream().map(FoodsModel::convertDtoToModel).collect(Collectors.toList());
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}
		return foodList;
	}

}
