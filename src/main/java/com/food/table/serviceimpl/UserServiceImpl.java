package com.food.table.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.food.table.constant.ApplicationConstants;
import com.food.table.constant.RestaurantStateEnum;
import com.food.table.dto.Restaurant;
import com.food.table.dto.UserAccount;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.FavoriteRestaurantModel;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.UserRepository;
import com.food.table.service.UserService;
import com.food.table.util.UserUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public void addFavoriteRestaurant(int restaurantId) {
		log.info("Entering add favorite restaurant " + restaurantId);
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		if (!restaurant.isPresent())
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		UserAccount userAccount = userRepository.findUserByPhoneNo(UserUtil.getUserDetails().getPhoneNo());
		List<Restaurant> favoriteRestaurants = userAccount.getFavoriteRestaurants();
		if (favoriteRestaurants == null) {
			favoriteRestaurants = List.of(restaurant.get());
		}
		favoriteRestaurants.add(restaurant.get());
		userAccount.setFavoriteRestaurants(favoriteRestaurants);
		try {
			userRepository.save(userAccount);
		} catch (DataIntegrityViolationException e) {
			log.info("Favorite restaurant already mapped to user" + restaurantId+", userId" + userAccount.getId());
		}

		log.info("Exiting add favorite restaurant " + restaurantId);
	}

	@Override
	public void deleteFavoriteRestaurant(int restaurantId) {
		log.info("Entering delete favorite restaurant " + restaurantId);
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		if (!restaurant.isPresent())
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		UserAccount userAccount = userRepository.findUserByPhoneNo(UserUtil.getUserDetails().getPhoneNo());
		List<Restaurant> favoriteRestaurants = userAccount.getFavoriteRestaurants();
		if (favoriteRestaurants != null) {
			favoriteRestaurants.removeIf(favoriteRestaurant -> favoriteRestaurant.getId() == restaurant.get().getId());
			userAccount.setFavoriteRestaurants(favoriteRestaurants);
			userRepository.save(userAccount);
		} else {
			log.info("Restaurant not exist " + restaurantId);
		}
		log.info("Exiting delete favorite restaurant " + restaurantId);
	}

	@Override
	public List<FavoriteRestaurantModel> getAllFavoriteRestaurant(String latitude, String longitude, int from,
			int limit) {
		log.info("Entering get all favorite restaurant from: " + from + " limit : " + limit);
		Pageable pageable = PageRequest.of(from, limit);
		UserAccount userAccount = userRepository.findUserByPhoneNo(UserUtil.getUserDetails().getPhoneNo());
		Page<FavoriteRestaurantModel> favoriteRestaurants = userRepository.findByFavoriteRestaurant(
				RestaurantStateEnum.getValue(RestaurantStateEnum.getValue(ApplicationConstants.confirmedState)),
				latitude, longitude, userAccount.getId(), pageable);

		log.info("Exiting get all confirmed restaurant from: " + from + " limit : " + limit + " is success");
		return favoriteRestaurants.getContent();
	}
}
