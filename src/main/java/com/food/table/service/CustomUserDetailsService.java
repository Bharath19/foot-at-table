package com.food.table.service;

import com.food.table.dto.Restaurant;
import com.food.table.dto.UserAccount;
import com.food.table.model.AuthRequest;
import com.food.table.model.AuthResponse;
import com.food.table.model.CustomerAuthRequest;
import com.food.table.model.RestaurantEmployeeRequestModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

    void checkAndCreateCustomerUser(CustomerAuthRequest authenticationRequest);

    void checkAndCreateCustomerUserByEmail(String email);

    boolean createRestaurantUser(AuthRequest authRequest, Restaurant restaurant, String userType);

    String createRefreshToken(long phoneNo);

    String createRefreshTokenByEmail(String email);

    AuthResponse getUserNameByRefreshToken(String refreshToken);

    void invalidateOtp(long phoneNo);

    boolean createMaintenanceUser(AuthRequest authRequest);

    UserAccount createEmployeeUser(RestaurantEmployeeRequestModel restaurantEmployeeRequestModel);

    boolean changeEmployeeUserStatus(String emailId, String status);

    int getRestaurantIdForUser(String emailId);


}
