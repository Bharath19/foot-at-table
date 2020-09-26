package com.food.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.CustomerAuthRequest;
import com.food.table.model.CustomerOtpRequest;
import com.food.table.model.UserProfileRequestModel;
import com.food.table.model.UserProfileResponseModel;
import com.food.table.service.CustomUserDetailsService;
import com.food.table.service.UserProfileService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserProfileController {

    final UserProfileService userProfileService;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Deprecated
    @ApiOperation(value = "Get User Profile By Phone Number", authorizations = {@Authorization(value = "accessToken")})
    @GetMapping("/profile/{phoneNo}")
    public ResponseEntity<UserProfileResponseModel> getUserProfileByPhoneNo(@PathVariable @NonNull long phoneNo) {
        return ResponseEntity.ok(userProfileService.getUserProfileByPhone(phoneNo));
    }
    
    @ApiOperation(value = "Get User Profile By ID", authorizations = {@Authorization(value = "accessToken")})
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseModel> getUserProfileById(@PathVariable @NonNull int id) {
        return ResponseEntity.ok(userProfileService.getUserProfileById(id));
    }

    @ApiOperation(value = "Edit User Profile", authorizations = {@Authorization(value = "accessToken")})
    @PutMapping("/profile/{phoneNo}")
    public ResponseEntity<UserProfileResponseModel> editUserProfile(@PathVariable @NonNull long phoneNo, @RequestBody UserProfileRequestModel userProfileRequestModel) {
        return ResponseEntity.ok(userProfileService.editProfile(phoneNo, userProfileRequestModel));
    }
    
	@ApiOperation(value = "Request OTP to edit user phone no", authorizations = {
			@Authorization(value = "accessToken") })
	@PostMapping("/profile/update/{userId}")
	public ResponseEntity<String> requestEditPhoneNO(@PathVariable @NonNull int userId,
			@RequestBody CustomerAuthRequest authenticationRequest) {
		log.info("OTP requested to edit phone no " + authenticationRequest.getPhoneNo() + "for user id :" + userId);
		if (authenticationRequest != null && authenticationRequest.getPhoneNo() != null) {
			customUserDetailsService.requestOtpToEditPhoneNo(authenticationRequest.getPhoneNo(), userId);
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_USER_PHONE_NO);
		}
		log.info("OTP sent successfully to edit phone no " + authenticationRequest.getPhoneNo() + "for user id :"
				+ userId);
		return ResponseEntity.ok("User Verified.Otp sent sucessfully");
	}
	
	@ApiOperation(value = "Request OTP to edit user phone no", authorizations = {
			@Authorization(value = "accessToken") })
	@PutMapping("/profile/update/{userId}")
	public ResponseEntity<String> updatePhoneNO(@PathVariable @NonNull int userId,
			@RequestBody CustomerOtpRequest customerOtpRequest) {
		log.info("OTP requested to edit phone no " + customerOtpRequest.getPhoneNo() + "for user id :" + userId);
		if (customerOtpRequest != null && customerOtpRequest.getPhoneNo() != null) {
			customUserDetailsService.updatePhoneNo(customerOtpRequest, userId);
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_USER_PHONE_NO);
		}
		log.info("OTP sent successfully to edit phone no " + customerOtpRequest.getPhoneNo() + "for user id :"
				+ userId);
		return ResponseEntity.ok("User Phone No Updated sucessfully");
	}

}
