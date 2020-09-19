package com.food.table.model;

import com.food.table.constant.PaymentMode;

import lombok.Data;

@Data
public class PaymentCallback {
	
	private String orderId;
	
    private String orderAmount;    
    
    private String referenceId;
    
    private String txStatus;
    
    private PaymentMode paymentMode;
    
    private String txMsg;
    
    private String txTime;
    
    private String signature;
}
