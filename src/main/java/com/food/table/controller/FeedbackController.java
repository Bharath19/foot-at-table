package com.food.table.controller;

import com.food.table.model.FeedbackModel;
import com.food.table.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/feedback")
@Api(value = "Feedback Management System")
@Slf4j
public class FeedbackController {

	@Autowired
	FeedbackService feedbackService;


	@PostMapping("/restaurant")
    @ApiOperation(value = "Add a user feedback for restauarant with corresponding restauarnt ID", authorizations = {@Authorization(value = "accessToken")})
	public ResponseEntity<Void> addRestaurantFeedback(@RequestParam(value = "restaurantId") Integer restaurantId,
			@Valid @RequestBody FeedbackModel feedbackModel) {
		log.info("Entering add restaurant feedback for restauarnt id : " + restaurantId);
		feedbackService.addRestaurantFeedback(restaurantId, feedbackModel);
		log.info("Exiting add restaurant feedback is success for restauarnt id : " + restaurantId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
