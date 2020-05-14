package com.food.table.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.FeedbackModel;
import com.food.table.service.FeedbackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/feedback")
@Api(value = "Feedback Management System")
@Slf4j
public class FeedbackController {

	@Autowired
	FeedbackService feedbackService;

	@PostMapping("/restaurant")
	@ApiOperation("Add a user feedback for restauarant with corresponding restauarnt ID")
	public ResponseEntity<Void> addRestaurantFeedback(@RequestParam(value = "restaurantId") Integer restaurantId,
			@Valid @RequestBody FeedbackModel feedbackModel) {
		log.info("Entering add restaurant feedback for restauarnt id : " + restaurantId);
		feedbackService.addRestaurantFeedback(restaurantId, feedbackModel);
		log.info("Exiting add restaurant feedback is success for restauarnt id : " + restaurantId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
