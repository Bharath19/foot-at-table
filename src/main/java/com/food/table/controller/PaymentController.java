package com.food.table.controller;

import com.food.table.constant.PaymentMode;
import com.food.table.model.PaymentCallback;
import com.food.table.model.PaymentDetail;
import com.food.table.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
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
	public @ResponseBody String payuCallback(@RequestParam String mihpayid, @RequestParam String status, @RequestParam PaymentMode mode, @RequestParam String txnid, @RequestParam String hash){
		PaymentCallback paymentCallback = new PaymentCallback();
		paymentCallback.setMihpayid(mihpayid);
		paymentCallback.setTxnid(txnid);
		paymentCallback.setMode(mode);
		paymentCallback.setHash(hash);
		paymentCallback.setStatus(status);
		return paymentService.payuCallback(paymentCallback);
	}
}
