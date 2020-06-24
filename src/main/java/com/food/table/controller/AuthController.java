package com.food.table.controller;

import com.food.table.dto.Restaurant;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.*;
import com.food.table.service.CustomUserDetailsService;
import com.food.table.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @ApiOperation(value = "Verifies Customer phone No for login")
    @RequestMapping(value = "auth/customer/login", method = RequestMethod.POST)
    public ResponseEntity<String> customerLogin(@RequestBody CustomerAuthRequest authenticationRequest) throws Exception {
        userDetailsService.checkAndCreateCustomerUser(authenticationRequest);
        log.info("User verified successfully and otp sent to phone no " + authenticationRequest.getPhoneNo());
        return ResponseEntity.ok("User Verified.Otp sent sucessfully");

    }

    @ApiOperation(value = "Verifies Otp for entered phoneNo")
    @RequestMapping(value = "auth/customer/verifyotp", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> verifyCustomerOtp(@RequestBody CustomerOtpRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getPhoneNo(), authenticationRequest.getOtp()));
        } catch (BadCredentialsException b) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_OTP);
        }
        final String jwt = jwtUtil.generateToken(String.valueOf(authenticationRequest.getPhoneNo()));
        userDetailsService.invalidateOtp(authenticationRequest.getPhoneNo());
        return ResponseEntity.ok(new AuthResponse(jwt, userDetailsService.createRefreshToken(authenticationRequest.getPhoneNo())));

    }

    @ApiOperation(value = "Restaurant user login api")
    @RequestMapping(value = "auth/restaurant/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> restaurantLogin(@RequestBody AuthRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException b) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_LOGIN);
        }
        return ResponseEntity.ok(userDetailsService.performRestaurantLogin(authenticationRequest));

    }

    @Deprecated
    @ApiOperation(value = "Restaurant user signup api")
    @RequestMapping(value = "auth/restaurant/signup", method = RequestMethod.POST)
    public ResponseEntity<String> restaurantSignUp(@RequestBody AuthRequest authenticationRequest) {
        if (userDetailsService.createRestaurantUser(authenticationRequest, new Restaurant(), StringUtils.EMPTY)) {
            log.info("User created Successfully for Email Id " + authenticationRequest.getUserName());
            return ResponseEntity.ok("User Created Successfully");
        } else {
            throw new BadCredentialsException("EmailId Already Taken");
        }

    }

    @ApiOperation(value = "Maintenance user signup api")
    @RequestMapping(value = "auth/maintenance/signup", method = RequestMethod.POST)
    public ResponseEntity<String> maintenanceUserSignUp(@RequestBody AuthRequest authenticationRequest) {
        if (userDetailsService.createMaintenanceUser(authenticationRequest)) {
            log.info("User created Successfully for Email Id " + authenticationRequest.getUserName());
            return ResponseEntity.ok("User Created Successfully");
        } else {
            throw new BadCredentialsException("EmailId Already Taken");
        }

    }

    @ApiOperation(value = "Validates user using refresh token")
    @RequestMapping(value = "auth/customer/refreshtoken", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> verifyRefreshToken(@RequestBody RefreshAuthRequest refreshAuthRequest) throws Exception {
        return ResponseEntity.ok(userDetailsService.getUserNameByRefreshToken(refreshAuthRequest.getToken()));

    }

    @ApiOperation(value = "Verifies Customer apple email id for login")
    @RequestMapping(value = "auth/customer/apple/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> customerAppleLogin(@RequestParam(value = "email", required = false) String email) throws Exception {
        userDetailsService.checkAndCreateCustomerUserByEmail(email);
        final String jwt = jwtUtil.generateToken(String.valueOf(email));
        return ResponseEntity.ok(new AuthResponse(jwt, userDetailsService.createRefreshTokenByEmail(email)));

    }
}
