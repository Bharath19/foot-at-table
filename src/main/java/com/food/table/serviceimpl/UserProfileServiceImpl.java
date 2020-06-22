package com.food.table.serviceimpl;

import com.food.table.dto.UserAccount;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.UserProfileRequestModel;
import com.food.table.model.UserProfileResponseModel;
import com.food.table.repo.UserRepository;
import com.food.table.service.UserProfileService;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    @Override
    public UserProfileResponseModel editProfile(long phoneNo, UserProfileRequestModel userProfileModel) {
        UserAccount userAccount = userRepository.findUserByPhoneNo(phoneNo);
        checkExistence(userAccount);
        if (StringUtils.isNotEmpty(userProfileModel.getName()))
            userAccount.setName(userProfileModel.getName());
        if (StringUtils.isNotEmpty(userProfileModel.getEmail()))
            userAccount.setEmail(userProfileModel.getEmail());
        UserAccount savedUserAccount = userRepository.save(userAccount);
        if (Objects.isNull(savedUserAccount))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.EDIT_USER_PROFILE_FAILED);
        return buildUserProfile(savedUserAccount);
    }

    @Override
    public UserProfileResponseModel getUserProfileByPhone(long phoneNo) {
        UserAccount userDetails = userRepository.findUserByPhoneNo(phoneNo);
        checkExistence(userDetails);
        return buildUserProfile(userDetails);
    }

	@Override
	public UserProfileResponseModel getUserProfileById(int id) {
        Optional<UserAccount> UserAccount = userRepository.findById(id);
        if (!UserAccount.isPresent())
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_USER_ID);
        return buildUserProfile(UserAccount.get());
	}
	
    private UserProfileResponseModel buildUserProfile(UserAccount userAccount) {
        return UserProfileResponseModel.builder()
                .email(userAccount.getEmail())
                .name(userAccount.getName())
                .phoneNo(ObjectUtils.defaultIfNull(userAccount.getPhoneNo(), 0L))
                .imageUrl(userAccount.getImageUrl())
                .build();

    }

    private void checkExistence(UserAccount userAccount) {
        if (Objects.isNull(userAccount)) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_USER_PHONE_NO);

        }

    }

}
