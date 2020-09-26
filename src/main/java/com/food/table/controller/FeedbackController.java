package com.food.table.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.FeedbackModel;
import com.food.table.service.FeedbackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/feedback")
@Api(value = "Feedback Management System")
@Slf4j
public class FeedbackController {

	@Autowired
	FeedbackService feedbackService;


	@PostMapping("/restaurant/{restaurantId}")
    @ApiOperation(value = "Add a user feedback for restauarant with corresponding restauarnt ID", authorizations = {@Authorization(value = "accessToken")})
	public ResponseEntity<Void> addRestaurantFeedback(@PathVariable(value = "restaurantId") Integer restaurantId,
			@Valid @RequestBody FeedbackModel feedbackModel) {
		log.info("Entering add restaurant feedback for restauarnt id : " + restaurantId);
		feedbackService.addRestaurantFeedback(restaurantId, feedbackModel);
		log.info("Exiting add restaurant feedback is success for restauarnt id : " + restaurantId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/restaurant/{restaurantId}/user/{userId}")
    @ApiOperation(value = "Add a user feedback for restauarant with corresponding restauarnt ID", authorizations = {@Authorization(value = "accessToken")})
	public ResponseEntity<List<FeedbackModel>> getRestaurantFeedbackByUser(@PathVariable(value = "restaurantId") Integer restaurantId,
			@PathVariable(value = "userId") Integer userId) {
		log.info("Entering get restaurant feedback for restauarnt id : " + restaurantId);
		List<FeedbackModel> feedBackModel = feedbackService.getRestaurantFeedbackByUser(restaurantId, userId);
		log.info("Exiting get restaurant feedback is success for restauarnt id : " + restaurantId);
		return ResponseEntity.ok(feedBackModel);
	}
}
