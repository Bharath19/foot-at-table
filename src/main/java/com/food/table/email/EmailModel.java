package com.food.table.email;

import org.springframework.beans.factory.annotation.Autowired;

import com.food.table.constant.ApplicationConstants;
import com.food.table.dto.Restaurant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailModel {
	
	@Autowired
  	private EmailService emailService;
	
	public static final String confirmRestaurant = "confirmRestaurant";
	
	private String to;
	private String subject;
	private String body;
	private String action;
	
	
	static EmailModel buildRestaurantTemplate(Restaurant restaurant) {
		EmailModel emailModel = new EmailModel();
		emailModel.setTo(restaurant.getRestaurantEmailId());
		emailModel.setTo("brootcountry@gmail.com");
		emailModel.setSubject("Restaurant confirmation");
		final String restaurantTemplateBody = restaurant.getRestaurantName()+" has been confirmed";
		emailModel.setBody(restaurantTemplateBody);
        return emailModel;
	}
	
	public static boolean canTriggerEmail(Restaurant restaurant, String actionType) {
		return actionType.equalsIgnoreCase(confirmRestaurant) && restaurant.getState().equalsIgnoreCase(ApplicationConstants.confirmedState);
	}

}