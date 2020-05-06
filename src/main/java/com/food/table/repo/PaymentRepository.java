package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{
	
	Payment findByTxnId(String txnId);
	
}
