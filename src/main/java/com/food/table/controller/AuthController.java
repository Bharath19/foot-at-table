package com.food.table.controller;

import com.food.table.model.*;
import com.food.table.service.CustomUserDetailsService;
import com.food.table.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
        return ResponseEntity.ok("User Verified.Otp sent sucessfully");

    }

    @ApiOperation(value = "Verifies Otp for entered phoneNo")
    @RequestMapping(value = "auth/customer/verifyotp", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> verifyCustomerOtp(@RequestBody CustomerOtpRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getPhoneNo(), authenticationRequest.getOtp()));
        } catch (BadCredentialsException b) {
            throw new Exception("Incorrect Username and password", b);
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
            throw new Exception("Incorrect Username and password", b);
        }
        final String jwt = jwtUtil.generateToken(authenticationRequest.getUserName());
        return ResponseEntity.ok(new AuthResponse(jwt));

    }

    @ApiOperation(value = "Restaurant user signup api")
    @RequestMapping(value = "auth/restaurant/signup", method = RequestMethod.POST)
    public ResponseEntity<String> restaurantSignUp(@RequestBody AuthRequest authenticationRequest) {
        if (userDetailsService.createRestaurantUser(authenticationRequest)) {
            return ResponseEntity.ok("sucess");
        } else {
            throw new BadCredentialsException("EmailId Already Taken");
        }

    }

    @ApiOperation(value = "Validates user using refresh token")
    @RequestMapping(value = "auth/customer/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<AuthResponse> verifyRefreshToken(@RequestBody RefreshAuthRequest refreshAuthRequest) throws Exception {
        Long phoneNo = userDetailsService.getUserNameByRefreshToken(refreshAuthRequest.getToken());
        final String jwt = jwtUtil.generateToken(String.valueOf(phoneNo));
        return ResponseEntity.ok(new AuthResponse(jwt, userDetailsService.createRefreshToken(phoneNo)));

    }
}
