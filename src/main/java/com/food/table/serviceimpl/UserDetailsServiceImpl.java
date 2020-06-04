package com.food.table.serviceimpl;

import com.food.table.constant.EmployeeStatusEnum;
import com.food.table.dto.Restaurant;
import com.food.table.dto.UserAccount;
import com.food.table.dto.UserRole;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.AuthRequest;
import com.food.table.model.CustomerAuthRequest;
import com.food.table.model.NotificationModel;
import com.food.table.model.RestaurantEmployeeRequestModel;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.UserRepository;
import com.food.table.repo.UserRoleRepository;
import com.food.table.service.CustomUserDetailsService;
import com.food.table.service.NotificationService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    final UserRepository userRepository;

    final UserRoleRepository userRoleRepository;

    final NotificationService notificationService;

    private LoadingCache<Long, Integer> otpCache;

    private final RestaurantRepository restaurantRepository;

    @Override
    public UserAccount loadUserByUsername(String user) throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = userRepository.findUserByEmailId(user);
        if (Objects.isNull(userAccount)) {
            return loadUserByPhoneNo(user, authentication);
        }
        validateUser(userAccount);
        return userAccount;
    }

    private UserAccount loadUserByPhoneNo(String user, Authentication authentication) {
        if (!NumberUtils.isParsable(user))
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.NO_USER_EMAIL_ID);
        UserAccount customerUserAccount = userRepository.findUserByPhoneNo(Long.parseLong(user));
        if (Objects.nonNull(authentication)) {
            int otp = getOtpFromCache(customerUserAccount.getPhoneNo());
            if (otp == 0)
                throw new BadCredentialsException("OTP is invalid");
            UserAccount userAccount = buildUserAccount(customerUserAccount, String.valueOf(otp));
            validateUser(userAccount);
            return userAccount;
        }
        UserAccount userAccount = buildUserAccount(customerUserAccount, StringUtils.EMPTY);
        validateUser(userAccount);
        return userAccount;
    }

    UserAccount buildUserAccount(UserAccount userAccount, String password) {
        return UserAccount.builder()
                .phoneNo(userAccount.getPhoneNo())
                .password(new BCryptPasswordEncoder().encode(String.valueOf(password)))
                .roles(userAccount.getRoles())
                .isAccountNonExpired(userAccount.isAccountNonExpired())
                .isAccountNonLocked(userAccount.isAccountNonLocked())
                .isCredentialsNonExpired(userAccount.isCredentialsNonExpired())
                .isEnabled(userAccount.isEnabled())
                .build();

    }

    private void validateUser(UserAccount userAccount) {
        if (!userAccount.isEnabled())
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.USER_DISABLED);
        if (!userAccount.isAccountNonExpired())
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.USER_EXPIRED);
        if (!userAccount.isAccountNonLocked())
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.USER_LOCKED);
        if (!userAccount.isCredentialsNonExpired())
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.USER_CREDENTIALS_EXPIRED);
    }

    @Override
    public void checkAndCreateCustomerUser(CustomerAuthRequest authenticationRequest) {
        UserAccount userAccount = userRepository.findUserByPhoneNo(authenticationRequest.getPhoneNo());

        if (Objects.isNull(userAccount)) {
            UserRole userRole = userRoleRepository.findRoleByName("CUSTOMER");
            List userRoleList = new LinkedList<>();
            userRoleList.add(userRole);
            UserAccount savedUserAccount = userRepository.save(UserAccount.builder()
                    .phoneNo(authenticationRequest.getPhoneNo())
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .isEnabled(true)
                    .roles(userRoleList)
                    .build());
            savedUserAccount.setUserId("CBUSER_" + savedUserAccount.getId());
            UserAccount userAccountresponse = userRepository.save(savedUserAccount);
            if (Objects.isNull(userAccountresponse))
                throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.USER_CREATION_FAILED);

        }
        generateOtp(authenticationRequest.getPhoneNo(), userAccount.getId());
    }

    @Override
    public boolean createRestaurantUser(AuthRequest authRequest, Restaurant restaurant) {
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        UserAccount userAccount = userRepository.findUserByEmailId(authRequest.getUserName());
        if (Objects.nonNull(userAccount)) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.DUPLICATE_EMAIL_USER);
        }
        verifyEmailIdFormat(authRequest.getUserName());
        UserRole userRole = userRoleRepository.findRoleByName("RESTAURANT_OWNER");
        List userRoleList = new LinkedList<>();
        userRoleList.add(userRole);
        UserAccount savedUserAccount = userRepository.save(UserAccount.builder()
                .email(authRequest.getUserName())
                .password(new BCryptPasswordEncoder().encode(authRequest.getPassword()))
                .restaurants(restaurantList)
                .roles(userRoleList)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(false)
                .build()
        );
        savedUserAccount.setUserId("CBUSER_" + savedUserAccount.getId());
        userRepository.save(savedUserAccount);
        return true;
    }

    @Override
    public String createRefreshToken(long phoneNo) {
        UserAccount userAccount = userRepository.findUserByPhoneNo(phoneNo);
        userAccount.setRefreshToken(String.valueOf(UUID.randomUUID()));
        return userRepository.save(userAccount).getRefreshToken();
    }

    @Override
    public long getUserNameByRefreshToken(String refreshToken) {
        UserAccount userAccount = userRepository.findUserByRefreshToken(refreshToken);
        if (Objects.isNull(userAccount))
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_REFRESH_TOKEN);
        return userAccount.getPhoneNo();
    }

    @Override
    public void invalidateOtp(long phoneNo) {
        otpCache.invalidate(phoneNo);
    }

    private void generateOtp(long phoneNo ,Integer userId) {
        int otp = RandomUtils.nextInt(1001, 9999);
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<Long, Integer>() {
            public Integer load(Long key) {
                return 0;
            }
        });
        otpCache.put(phoneNo, otp);
        NotificationModel smsNotification = NotificationModel.builder().notificationText("Your otp is " + otp).notificationType("sms").userId(userId).build();
        notificationService.publish(smsNotification);
    }

    @Override
    public boolean createMaintenanceUser(AuthRequest authRequest) {
        UserAccount userAccount = userRepository.findUserByEmailId(authRequest.getUserName());
        if (Objects.nonNull(userAccount)) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.DUPLICATE_EMAIL_USER);
        }
        verifyEmailIdFormat(authRequest.getUserName());
        UserRole userRole = userRoleRepository.findRoleByName("ADMIN");
        List userRoleList = new LinkedList<>();
        userRoleList.add(userRole);
        UserAccount savedUserAccount = userRepository.save(UserAccount.builder()
                .email(authRequest.getUserName())
                .password(new BCryptPasswordEncoder().encode(authRequest.getPassword()))
                .roles(userRoleList)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build()
        );
        savedUserAccount.setUserId("CBUSER_" + savedUserAccount.getId());
        userRepository.save(savedUserAccount);
        return true;
    }

    @Override
    public UserAccount createEmployeeUser(RestaurantEmployeeRequestModel restaurantEmployeeRequestModel) {
        log.info("Entering createEmployeeUser method for email Id" + restaurantEmployeeRequestModel.getEmailId());
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantEmployeeRequestModel.getRestaurantId());
        if (!restaurant.isPresent()) {
            log.error("Invalid restaurantId entered: " + restaurantEmployeeRequestModel.getRestaurantId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_RESTAURANT_ID);
        }
        UserAccount userAccount = userRepository.findUserByEmailId(restaurantEmployeeRequestModel.getEmailId());
        if (Objects.nonNull(userAccount)) {
            log.error("EmailId is already present: " + restaurantEmployeeRequestModel.getEmailId());
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.DUPLICATE_EMAIL_USER);
        }
        verifyEmailIdFormat(restaurantEmployeeRequestModel.getEmailId());
        UserRole userRole = userRoleRepository.findRoleByName(restaurantEmployeeRequestModel.getRole());
        if (Objects.isNull(userRole)) {
            log.error("Invalid User Role: " + restaurantEmployeeRequestModel.getRole());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_USER_ROLE);

        }
        List userRoleList = new LinkedList<>();
        userRoleList.add(userRole);
        List<Restaurant> restaurantList = new LinkedList<>();
        restaurantList.add(restaurant.get());
        UserAccount savedUserAccount = userRepository.save(UserAccount.builder()
                .email(restaurantEmployeeRequestModel.getEmailId())
                .password(new BCryptPasswordEncoder().encode(restaurantEmployeeRequestModel.getPassword()))
                .roles(userRoleList)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .restaurants(restaurantList)
                .build()
        );
        if (Objects.isNull(savedUserAccount)) {
            log.error("createEmployeeUser failed for EmailId: " + restaurantEmployeeRequestModel.getEmailId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.USER_CREATION_FAILED);
        }
        savedUserAccount.setUserId("CBUSER_" + savedUserAccount.getId());
        UserAccount finalUserAccount = userRepository.save(savedUserAccount);
        if (Objects.isNull(finalUserAccount)) {
            log.error("createEmployeeUser failed for EmailId: " + restaurantEmployeeRequestModel.getEmailId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.USER_CREATION_FAILED);
        }
        log.info("Finished createEmployeeUser method successfully for emailId:" + restaurantEmployeeRequestModel.getEmailId());
        return finalUserAccount;
    }

    @Override
    public boolean changeEmployeeUserStatus(String emailId, String status) {
        UserAccount userAccount = userRepository.findUserByEmailId(emailId);
        if (status.equalsIgnoreCase(EmployeeStatusEnum.ACTIVE.getName())) {
            userAccount.setIsEnabled(true);
        } else {
            userAccount.setIsEnabled(false);
        }
        return true;
    }

    @Override
    public int getRestaurantIdForUser(String emailId) {
        UserAccount userAccount = userRepository.findUserByEmailId(emailId);
        if (CollectionUtils.isNotEmpty(userAccount.getRestaurants()))
            return userAccount.getRestaurants().get(0).getId();
        return 0;
    }

    private int getOtpFromCache(long phoneNo) {
        try {
            if (Objects.isNull(otpCache))
                throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.OTP_FETCH_FAILED);
            return otpCache.get(phoneNo);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void verifyEmailIdFormat(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            log.error("Invalid Email Format: " + email);
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_EMAIL_FORMAT);
        }
    }
}
