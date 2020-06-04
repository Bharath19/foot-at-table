package com.food.table.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.food.table.dto.UserAccount;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserUtil {
	
	@Autowired
	private UserRepository userRepository;

	public static UserAccount getUserDetails() {
		try {
			return (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch (Exception e) {
			log.info("Error while extracting user from token" + e.getStackTrace());
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_ID);
		}
	}
	
	public Integer getCurrentUserId() {
		return userRepository.findUserByPhoneNo(getUserDetails().getPhoneNo()).getId();
	}
}
