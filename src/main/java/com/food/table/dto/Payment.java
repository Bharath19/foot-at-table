package com.food.table.dto;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
	    
	    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    @JoinColumn(name = "order_id", referencedColumnName = "id")
	    private Order order;
	    
	    @Enumerated(EnumType.STRING)
	    private PaymentStatus paymentStatus;
	    
	    @Temporal(TemporalType.DATE)
	    private Date paymentDate;
	    
	    private String txnId;
	    
	    private String referenceId;
	    
	    private String txMsg;
	    
	    private String signature;
	    
	    @Enumerated(EnumType.STRING)
	    private PaymentMode paymentMode;

}
