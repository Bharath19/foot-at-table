package com.food.table.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import com.food.table.dto.UserAccount;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserUtil {

	public static UserAccount getUserDetails() {
		try {
			return (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch (Exception e) {
			log.info("Error while extracting user from token" + e.getStackTrace());
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_ID);
		}
	}
}
