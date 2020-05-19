package com.food.table.service;

import com.food.table.model.AuthRequest;
import com.food.table.model.CustomerAuthRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

    void checkAndCreateCustomerUser(CustomerAuthRequest authenticationRequest);

    boolean createRestaurantUser(AuthRequest authRequest);

    String createRefreshToken(long phoneNo);

    long getUserNameByRefreshToken(String refreshToken);

    void invalidateOtp(long phoneNo);


}
