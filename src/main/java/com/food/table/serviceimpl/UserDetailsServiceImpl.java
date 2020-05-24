package com.food.table.serviceimpl;

import com.food.table.dto.UserAccount;
import com.food.table.dto.UserRole;
import com.food.table.exceptions.RecordNotFoundException;
import com.food.table.model.AuthRequest;
import com.food.table.model.CustomerAuthRequest;
import com.food.table.model.NotificationModel;
import com.food.table.repo.UserRepository;
import com.food.table.repo.UserRoleRepository;
import com.food.table.service.CustomUserDetailsService;
import com.food.table.service.NotificationService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    final UserRepository userRepository;

    final UserRoleRepository userRoleRepository;

    final NotificationService notificationService;

    private LoadingCache<Long, Integer> otpCache;

    @Override
    public UserAccount loadUserByUsername(String user) throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = userRepository.findUserByEmailId(user);
        if (Objects.isNull(userAccount)) {
            return loadUserByPhoneNo(user, authentication);
        }
        return userAccount;
    }

    private UserAccount loadUserByPhoneNo(String user, Authentication authentication) {
        UserAccount customerUserAccount = userRepository.findUserByPhoneNo(Long.parseLong(user));
        if (Objects.nonNull(authentication)) {
            int otp = getOtpFromCache(customerUserAccount.getPhoneNo());
            if (otp == 0)
                throw new BadCredentialsException("OTP is invalid");
            return buildUserAccount(customerUserAccount, String.valueOf(otp));
        }
        return buildUserAccount(customerUserAccount, StringUtils.EMPTY);
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
            userRepository.save(savedUserAccount);
        }
        generateOtp(authenticationRequest.getPhoneNo());
    }

    @Override
    public boolean createRestaurantUser(AuthRequest authRequest) {
        UserAccount userAccount = userRepository.findUserByEmailId(authRequest.getUserName());
        if (Objects.nonNull(userAccount)) {
            throw new BadCredentialsException("Email Id already Taken");
        }
        UserRole userRole = userRoleRepository.findRoleByName("RESTAURANT_OWNER");
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
    public String createRefreshToken(long phoneNo) {
        UserAccount userAccount = userRepository.findUserByPhoneNo(phoneNo);
        userAccount.setRefreshToken(String.valueOf(UUID.randomUUID()));
        return userRepository.save(userAccount).getRefreshToken();
    }

    @Override
    public long getUserNameByRefreshToken(String refreshToken) {
        UserAccount userAccount = userRepository.findUserByRefreshToken(refreshToken);
        if (Objects.isNull(userAccount))
            throw new RecordNotFoundException("No User Account found");
        return userAccount.getPhoneNo();
    }

    @Override
    public void invalidateOtp(long phoneNo) {
        otpCache.invalidate(phoneNo);
    }

    private void generateOtp(long phoneNo) {
        int otp = RandomUtils.nextInt(1001, 9999);
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<Long, Integer>() {
            public Integer load(Long key) {
                return 0;
            }
        });
        otpCache.put(phoneNo, otp);
        NotificationModel smsNotification = NotificationModel.builder().notificationText("Your otp is " + otp).notificationType("sms").recipientId(+91 + String.valueOf(phoneNo)).build();
        notificationService.publish(smsNotification);
    }

    private int getOtpFromCache(long phoneNo) {
        try {
            return otpCache.get(phoneNo);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
