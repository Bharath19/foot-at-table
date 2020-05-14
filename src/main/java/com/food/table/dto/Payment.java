package com.food.table.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.food.table.constant.PaymentMode;
import com.food.table.constant.PaymentStatus;

import lombok.Data;

@Entity
@Data
public class Payment {
	

	    @Id	    
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    
	    private String email;
	    
	    private String name;
	    
	    private String phone;
	    
	    private String productInfo;
	    
	    private Double amount;
	    
	    @Enumerated(EnumType.STRING)
	    private PaymentStatus paymentStatus;
	    
	    @Temporal(TemporalType.DATE)
	    private Date paymentDate;
	    
	    private String txnId;
	    
	    private String mihpayId;
	    
	    @Enumerated(EnumType.STRING)
	    private PaymentMode mode;

}
