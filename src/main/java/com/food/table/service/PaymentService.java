package com.food.table.service;

import org.springframework.stereotype.Service;

import com.food.table.model.PaymentCallback;
import com.food.table.model.PaymentDetail;

@Service
public interface PaymentService {
	
	public PaymentDetail proceedPayment(PaymentDetail paymentDetail);
	
	public String payuCallback(PaymentCallback paymentResponse);
}
