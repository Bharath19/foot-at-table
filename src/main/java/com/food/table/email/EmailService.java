package com.food.table.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.food.table.dto.Restaurant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

	@Autowired
  	private JavaMailSender mailSender;
	
	@Autowired
  	private EmailService emailService;
	
	public void sendMail(EmailModel emailModel) {
		if(emailModel != null && emailModel.getTo() != null) {
			SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(emailModel.getTo());
	        message.setSubject(emailModel.getSubject());
	        message.setText(emailModel.getBody());
	        mailSender.send(message);
		}else {
			log.error("Invalid Email Template object " + emailModel);
		}
	}
	
	public void triggerEmail(Restaurant restaurant, String action) {
		if(EmailModel.canTriggerEmail(restaurant, action)) {
			emailService.sendMail(EmailModel.buildRestaurantTemplate(restaurant));
		}
	}

}