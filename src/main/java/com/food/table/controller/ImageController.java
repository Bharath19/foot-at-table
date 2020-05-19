package com.food.table.controller;

import com.food.table.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@Api(value = "Image Management System")
@Slf4j
public class ImageController {

	@Autowired
	ImageService imageService;

	@ApiOperation(value = "Upload a image file to s3 for respective restaurants")
	@PostMapping("/upload")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> uploadImagefile(@RequestParam("restaurantId") Integer restaurantId,
			@RequestParam(value="foodId",required = false) Integer foodId,@RequestParam(value="extrasId",required = false) Integer extrasId,@RequestParam("file") MultipartFile file) {
		log.info("Upload image file to s3 for restaurant : " + restaurantId);
		imageService.uploadFile(file, restaurantId, foodId,extrasId);
		log.info("Upload image file to s3 is success for restaurant : " + restaurantId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
