package com.food.table.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.OfferResponseModel;
import com.food.table.model.OffersModel;
import com.food.table.model.UserOfferMonitorResponse;
import com.food.table.model.ValidateCouponRequest;
import com.food.table.model.ValidateCouponResponse;
import com.food.table.service.OfferService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/offers")
@Slf4j
@Api(value="Offer Management System")
public class OfferController {

	@Autowired
	private OfferService offerservice;

	@ApiOperation(value = "Add new Offers", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/add")
	public ResponseEntity<Void> addOffers(@Valid @RequestBody OffersModel offersModel) {
		log.info("Entering add Offers ");
		offerservice.addoffers(offersModel);
		log.info("Entering add Offers is success ");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Get Coupouns for User", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/users")
	public ResponseEntity<List<OfferResponseModel>> getCouponsForUser(
			@RequestParam(value = "restaurantId") Integer restaurantId) {
		log.info(" Entering get Coupouns for restaurant Id : "+restaurantId);
		List<OfferResponseModel> offerResponseModel = offerservice.getCouponsForUser(restaurantId);
		log.info(" Entering get Coupouns is success for restaurant Id : "+restaurantId);
		return ResponseEntity.ok(offerResponseModel);
	}
	
	@ApiOperation(value = "Validate Coupouns for User", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping("/validateCoupoun")
	public ResponseEntity<ValidateCouponResponse> validateCoupoun(@RequestBody @Valid ValidateCouponRequest validateCouponRequest) {
		log.info(" Entering validate Coupouns for User and restaurant Id : "+validateCouponRequest.getRestaurantId());
		ValidateCouponResponse validateCouponResponse = offerservice.validateCouponsService(validateCouponRequest);
		log.info(" Entering validate Coupouns is success for User and restaurant Id : "+validateCouponRequest.getRestaurantId());
		return ResponseEntity.ok(validateCouponResponse);
	}
	
	@ApiOperation(value = "Get Used Coupouns for User", authorizations = {@Authorization(value = "accessToken")})
	@GetMapping("/getRewardsInfo")
	public ResponseEntity<List<UserOfferMonitorResponse>> getRewardsInfo() {
		log.info(" Entering get Used Coupouns for User : ");
		List<UserOfferMonitorResponse> userOfferResponse=offerservice.getUserUsedOffers();
		log.info(" Entering get Used Coupouns is success for User : ");
		return ResponseEntity.ok(userOfferResponse);
	}
}
