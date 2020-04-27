package com.food.table.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.food.table.dto.Payments;
import com.food.table.dto.Seatings;
import com.food.table.dto.Services;
import com.food.table.dto.Types;
import com.food.table.dto.constant.ApplicationConstants;
import com.food.table.repo.PaymentsRepository;
import com.food.table.repo.SeatingsRepository;
import com.food.table.repo.ServiceRepository;
import com.food.table.repo.TypesRepository;

@Component
public class DefaultTableValues {
	
	@Autowired
	SeatingsRepository seatingsRepository;
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired
	PaymentsRepository paymentsRepository;
	
	@Autowired
	TypesRepository typesRepository;
	
	@PostConstruct
	public void init() {
		List<Payments> payment=paymentsRepository.findAll();
		List<Seatings> seating=seatingsRepository.findAll();
		List<Services> service=serviceRepository.findAll();
		List<Types> type=typesRepository.findAll();
		loadDefaultData(payment, seating, service, type);
	}
	
	private void loadDefaultData(List<Payments> payment, List<Seatings> seating, List<Services> service,
			List<Types> type) {
		
		if(payment.isEmpty() || payment.size()<=0) {
			List<String> paymentValue=ApplicationConstants.payments;
			paymentValue.forEach(val->{
				payment.add(Payments.builder().name(val).build());
			});
			paymentsRepository.saveAll(payment);
		}
		
		if(seating.isEmpty() || seating.size()<=0) {
			List<String> seatingValue=ApplicationConstants.seatings;
			seatingValue.forEach(val->{
				seating.add(Seatings.builder().name(val).build());
			});
			seatingsRepository.saveAll(seating);
		}
		
		if(service.isEmpty() || service.size()<=0) {
			List<String> serviceValue=ApplicationConstants.services;
			serviceValue.forEach(val->{
				service.add(Services.builder().name(val).build());
			});
			serviceRepository.saveAll(service);
		}
		
		if(type.isEmpty() || type.size()<=0) {
			List<String> paymentValue=ApplicationConstants.types;
			paymentValue.forEach(val->{
				type.add(Types.builder().name(val).build());
			});
			typesRepository.saveAll(type);
		}
	}
	
}
