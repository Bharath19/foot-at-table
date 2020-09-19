package com.food.table.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.table.dto.Order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDetail {
	
	private String appId;
	
    private String orderId;
    
    private String orderAmount;
    
    private String orderCurrency;
    
    private String orderNote;
    
    private String customerName;
    
    private String customerPhone;
    
    private String customerEmail;
    
    private String returnUrl;
    
    private String notifyUrl;
    
    private String signature;
    
    @JsonIgnore
    private Order order;
}
