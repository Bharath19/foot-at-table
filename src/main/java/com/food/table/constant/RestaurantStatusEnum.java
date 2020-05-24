package com.food.table.constant;

import org.springframework.http.HttpStatus;

import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;

public enum RestaurantStatusEnum {
	OPEN("Open"), CLOSED("Closed");

	private String value;

	RestaurantStatusEnum(String value) {
		this.value = value;
	}

	public String toString() {
		return String.valueOf(value);
	}

	public static String getValue(String text) {
		for (RestaurantStatusEnum b : RestaurantStatusEnum.values()) {
			if (String.valueOf(b.value).equalsIgnoreCase(text)) {
				return b.value;
			}
		}
		throw new ApplicationException(HttpStatus.BAD_REQUEST,  ApplicationErrors.INVALID_STATUS_VALUE);
	}
}
