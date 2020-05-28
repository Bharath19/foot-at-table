package com.food.table.exception;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorResource> handleApplicationClientException(ApplicationException ex) {
		ErrorResource errorResource = convertTo(ex.getStatus(), ex.getMessage(), ex);
		Map<String, String> fieldErrors = ex.getArgs();
		Set<Entry<String, String>> entrySet = fieldErrors.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			errorResource.putFieldError(entry.getKey(), entry.getValue(), null, null);

		}
		return new ResponseEntity<>(errorResource, ex.getStatus());
	}
	

	
	private ErrorResource convertTo(HttpStatus status, String message, Throwable ex) {
		return new ErrorResource(status, message, ex);
	}
}
