package com.food.table.controller;

import com.food.table.model.UserProfileRequestModel;
import com.food.table.model.UserProfileResponseModel;
import com.food.table.service.UserProfileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserProfileController {

    final UserProfileService userProfileService;

    @Deprecated
    @ApiOperation(value = "Get User Profile By Phone Number", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/profile/{phoneNo}", method = RequestMethod.GET)
    public ResponseEntity<UserProfileResponseModel> getUserProfileByPhoneNo(@PathVariable @NonNull long phoneNo) {
        return ResponseEntity.ok(userProfileService.getUserProfileByPhone(phoneNo));
    }
    
    @ApiOperation(value = "Get User Profile By ID", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserProfileResponseModel> getUserProfileById(@PathVariable @NonNull int id) {
        return ResponseEntity.ok(userProfileService.getUserProfileById(id));
    }

    @ApiOperation(value = "Edit User Profile", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/profile/{phoneNo}", method = RequestMethod.PUT)
    public ResponseEntity<UserProfileResponseModel> editUserProfile(@PathVariable @NonNull long phoneNo, @RequestBody UserProfileRequestModel userProfileRequestModel) {
        return ResponseEntity.ok(userProfileService.editProfile(phoneNo, userProfileRequestModel));
    }

}
