package com.food.table.constant;

import org.springframework.http.HttpStatus;

import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;

public enum OfferStateEnum {
	ACTIVE("Active"), INACTIVE("Inactive");

	private String value;

	OfferStateEnum(String value) {
		this.value = value;
	}

	public String toString() {
		return String.valueOf(value);
	}

	public static String getValue(String text) {
		for (OfferStateEnum b : OfferStateEnum.values()) {
			if (String.valueOf(b.value).equalsIgnoreCase(text)) {
				return b.value;
			}
		}
		throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_STATE_VALUE);
	}
}
