package com.food.table.service;

import com.food.table.model.UserProfileRequestModel;
import com.food.table.model.UserProfileResponseModel;

public interface UserProfileService {

    UserProfileResponseModel editProfile(long phoneNo, UserProfileRequestModel userProfileModel);

    UserProfileResponseModel getUserProfileByPhone(long phoneNo);

    UserProfileResponseModel getUserProfileById(int id);
}
