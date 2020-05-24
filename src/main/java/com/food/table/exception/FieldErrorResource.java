package com.food.table.exception;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class FieldErrorResource {

	private final String field;

	private final Object attemptedValue;

	private List<ErrorMessage> errors;

	public FieldErrorResource(String field, Object attemptedValue) {
		this.field = field;
		this.attemptedValue = attemptedValue;
	}

	public void addError(String code, String message) {
		if (this.errors == null) {
			this.errors = new ArrayList<>();
		}
		this.errors.add(new ErrorMessage(code, message));
	}

	@Data
	@AllArgsConstructor
	@JsonInclude(Include.NON_NULL)
	public class ErrorMessage {

		private final String code;

		private final String message;
	}
}
