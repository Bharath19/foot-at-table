package com.food.table.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
	
	 private static final long serialVersionUID = 1L;
	 
	 private final HttpStatus status;  
	 
	 private final String message;
	 
	 private final Map<String, String> args = new HashMap<>();
	 
	 public ApplicationException(HttpStatus status,String message) {
		 this.status=status;
		 this.message=message;
	 }
	 
	 public ApplicationException(HttpStatus status,String message,Map<String, String> args) {
		 this.status=status;
		 this.message=message;
		 if (args != null)
			{
				this.args.putAll(args);
			}
	 }
	 
	 public ApplicationException(Throwable cause,HttpStatus status,String message) {
		 super(cause);
		 this.status=status;
		 this.message=message;
		 if (args != null)
			{
				this.args.put("exception", cause.toString());
			}
	 }
}
