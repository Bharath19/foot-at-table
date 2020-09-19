package com.food.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.PaymentCallback;
import com.food.table.model.PaymentDetail;
import com.food.table.service.PaymentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {
	
	    @Autowired
	    private PaymentService paymentService;

	@ApiOperation(value = "", authorizations = {@Authorization(value = "accessToken")})
	@PostMapping(path = "/payment-details")
	public @ResponseBody PaymentDetail proceedPayment(@RequestBody PaymentDetail paymentDetail){
		return paymentService.proceedPayment(paymentDetail);
	}

	@ApiOperation(value = "", authorizations = {@Authorization(value = "accessToken")})
	@RequestMapping(path = "/payment-response", method = RequestMethod.POST)
	public @ResponseBody String payuCallback(@RequestBody PaymentCallback paymentCallback){
		long startTime=System.currentTimeMillis();
		log.info("Entering payuCallback starttime : "+startTime);		
		String response = paymentService.payuCallback(paymentCallback);
		long endTime=System.currentTimeMillis();
		log.info("Exiting payuCallback is success and timetaken : "+(endTime-startTime));
		return response;
	}
}
