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

	public UserAccount getCurrentUserId() {
		try {
			UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(userAccount.getPhoneNo() != null) {
				userAccount  = userRepository.findUserByPhoneNo(userAccount.getPhoneNo());
			}else if (userAccount.getEmail() != null){
				userAccount  = userRepository.findUserByEmailId(userAccount.getEmail());
			}
			if(userAccount.getId() != 0) {
				return userAccount;
			}
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_CURRENT_USER);
		}catch (Exception e) {
			log.info("Error while extracting user from token" + e.getStackTrace());
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_CURRENT_USER);
		}
	}
}
