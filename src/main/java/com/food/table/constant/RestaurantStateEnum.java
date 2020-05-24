package com.food.table.constant;

import org.springframework.http.HttpStatus;

import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;

public enum RestaurantStateEnum {
	CONFIRMED("Confirmed"), DRAFTED("Drafted"), DELETED("Deleted");

	private String value;

	RestaurantStateEnum(String value) {
		this.value = value;
	}

	public String toString() {
		return String.valueOf(value);
	}

	public static String getValue(String text) {
		for (RestaurantStateEnum b : RestaurantStateEnum.values()) {
			if (String.valueOf(b.value).equalsIgnoreCase(text)) {
				return b.value;
			}
		}
		throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_STATE_VALUE);
	}
}
