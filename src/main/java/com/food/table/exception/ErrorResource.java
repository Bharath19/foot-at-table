package com.food.table.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@JsonInclude(Include.NON_NULL)
public class ErrorResource {

	private final int status;

	private final String message;

	private final Date timestamp;

	private final String developerMessage;
	
	 private List<FieldErrorResource> fieldErrors;

	public ErrorResource(HttpStatus status, String message, Throwable ex) {
		this.status = status.value();
		this.message = message;
		this.timestamp = new Date();
		this.developerMessage = log.isDebugEnabled() ? this.getDeveloperMessage(ex) : null;
	}
	
	public void putFieldError(String field, Object attemptedValue, String code, String message) {
		if (this.fieldErrors != null) {
			FieldErrorResource fieldError = this.getFieldError(field);
			if (fieldError == null) {
				this.addFieldError(field, attemptedValue, code, message);
			} else if (code != null || message != null) {
				fieldError.addError(code, message);
			}
		} else {
			this.fieldErrors = new ArrayList<>();
			this.addFieldError(field, attemptedValue, code, message);
		}
	}
	
	private void addFieldError(String field, Object attemptedValue, String code, String message) {
		FieldErrorResource resource = new FieldErrorResource(field, attemptedValue);
		if (code != null || message != null) {
			resource.addError(code, message);
		}
		this.fieldErrors.add(resource);
	}

    
    private FieldErrorResource getFieldError(String field) {
        for (FieldErrorResource fieldError : this.fieldErrors) {
            if (fieldError.getField().equals(field)) {
                return fieldError;
            }
        }
        return null;
    }
    
	private String getDeveloperMessage(Throwable ex) {
		StringWriter errorStackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(errorStackTrace));
		return errorStackTrace.toString();
	}

}
