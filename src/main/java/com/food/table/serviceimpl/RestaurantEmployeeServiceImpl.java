package com.food.table.serviceimpl;

import com.food.table.constant.EmployeeStatusEnum;
import com.food.table.dto.Restaurant;
import com.food.table.dto.RestaurantEmployee;
import com.food.table.dto.UserAccount;
import com.food.table.dto.UserRole;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.RestaurantEmployeeEditModel;
import com.food.table.model.RestaurantEmployeeRequestModel;
import com.food.table.model.RestaurantEmployeeResponseModel;
import com.food.table.repo.RestaurantEmployeeRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.UserRepository;
import com.food.table.repo.UserRoleRepository;
import com.food.table.service.CustomUserDetailsService;
import com.food.table.service.RestaurantEmployeeService;
import com.food.table.util.AuthorityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantEmployeeServiceImpl implements RestaurantEmployeeService {

    private final RestaurantEmployeeRepository restaurantEmployeeRepository;

    private final CustomUserDetailsService userDetailsService;

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final AuthorityUtil authorityUtil;

    @Override
    public RestaurantEmployeeResponseModel insertNewEmployee(RestaurantEmployeeRequestModel restaurantEmployeeRequestModel) {
        log.info("Entered insertNewEmployee method for email Id" + restaurantEmployeeRequestModel.getEmailId());
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantEmployeeRequestModel.getRestaurantId());
        if (!restaurant.isPresent()) {
            log.error("Invalid restaurantId entered: " + restaurantEmployeeRequestModel.getRestaurantId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_RESTAURANT_ID);
        }
        checkAuthority(restaurant.get().getId());
        UserAccount savedUserAccount = userDetailsService.createEmployeeUser(restaurantEmployeeRequestModel);
        if (Objects.isNull(savedUserAccount)) {
            log.error("No User found for EmailId: " + restaurantEmployeeRequestModel.getEmailId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_USER_ID);
        }
        RestaurantEmployee savedEmployee = restaurantEmployeeRepository.save(buildEmployeeModel(restaurantEmployeeRequestModel, savedUserAccount));
        if (Objects.isNull(savedEmployee)) {
            userRepository.delete(savedUserAccount);
            log.error("insertNewEmployee method failed for EmailId: " + restaurantEmployeeRequestModel.getEmailId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.EMPLOYEE_CREATION_FAILED);
        }
        savedEmployee.setEmployeeId("EMP_" + savedEmployee.getId());
        RestaurantEmployee finalEmployee = restaurantEmployeeRepository.save(savedEmployee);
        if (Objects.isNull(finalEmployee)) {
            userRepository.delete(savedUserAccount);
            log.error("insertNewEmployee method failed for EmailId: " + restaurantEmployeeRequestModel.getEmailId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.EMPLOYEE_CREATION_FAILED);
        }
        log.info("Finished insertNewEmployee method successfully for email Id" + restaurantEmployeeRequestModel.getEmailId());
        return buildResponseModel(savedEmployee);

    }

    private RestaurantEmployee buildEmployeeModel(RestaurantEmployeeRequestModel restaurantEmployeeRequestModel, UserAccount userAccount) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantEmployeeRequestModel.getRestaurantId());
        if (!restaurant.isPresent()) {
            log.error("Invalid restaurantId entered: " + restaurantEmployeeRequestModel.getRestaurantId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_RESTAURANT_ID);
        }
        return RestaurantEmployee.builder().name(restaurantEmployeeRequestModel.getName())
                .employeeId(StringUtils.EMPTY)
                .email(restaurantEmployeeRequestModel.getEmailId())
                .phoneNo(restaurantEmployeeRequestModel.getPhoneNo())
                .deviceId(restaurantEmployeeRequestModel.getDeviceId())
                .deviceName(restaurantEmployeeRequestModel.getDeviceName())
                .gender(restaurantEmployeeRequestModel.getGender())
                .restaurant(restaurant.get())
                .age(restaurantEmployeeRequestModel.getAge())
                .status(EmployeeStatusEnum.getValue(restaurantEmployeeRequestModel.getStatus()))
                .user(userAccount)
                .bloodGroup(restaurantEmployeeRequestModel.getBloodGroup())
                .build();
    }

    @Override
    public RestaurantEmployeeResponseModel updateEmployee(int id, RestaurantEmployeeEditModel restaurantEmployeeRequestModel) {
        log.info("Entered updateEmployee method for Id " + id);
        RestaurantEmployee fetchedRestaurantEmployee = restaurantEmployeeRepository.findEmployeeById(id);
        if (Objects.isNull(fetchedRestaurantEmployee)) {
            log.error("No Records Present in restaurant_employee table for : " + id);
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_EMPLOYEE_ID);
        }
        checkAuthority(fetchedRestaurantEmployee.getRestaurant().getId());
        UserRole userRole = userRoleRepository.findRoleByName(restaurantEmployeeRequestModel.getRole());
        if (Objects.isNull(userRole)) {
            log.error("Invalid User Role: " + restaurantEmployeeRequestModel.getRole());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_USER_ROLE);
        }
        UserAccount userAccount = userRepository.findUserByEmailId(fetchedRestaurantEmployee.getEmail());
        if (Objects.isNull(userAccount)) {
            log.error("No Records Present in user_account table for Email : " + fetchedRestaurantEmployee.getEmail());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_EMPLOYEE_ID);
        }
        Optional<Restaurant> restaurant = restaurantRepository.findById(fetchedRestaurantEmployee.getRestaurant().getId());
        if (!restaurant.isPresent()) {
            log.error("No records found in restaurant table for id: " + fetchedRestaurantEmployee.getRestaurant().getId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_RESTAURANT_ID);
        }
        RestaurantEmployee restaurantEmployee = buildEditEmployeeModel(restaurantEmployeeRequestModel, userAccount, fetchedRestaurantEmployee);
        restaurantEmployee.setEmployeeId(fetchedRestaurantEmployee.getEmployeeId());
        restaurantEmployee.setId(fetchedRestaurantEmployee.getId());
        RestaurantEmployee savedEmployee = restaurantEmployeeRepository.save(restaurantEmployee);
        if (Objects.isNull(savedEmployee)) {
            log.error("updateNewEmployee method failed for Id: " + id);
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.EMPLOYEE_CREATION_FAILED);
        }
        UserAccount userAccountChange = userRepository.findUserByEmailId(savedEmployee.getEmail());
        List<UserRole> roleList = new LinkedList<>();
        roleList.add(userRole);
        userAccountChange.setRoles(roleList);
        UserAccount savedUserAccount = userRepository.save(userAccountChange);
        if (Objects.isNull(savedUserAccount)) {
            log.error("Updation for user failed for EmailId: " + fetchedRestaurantEmployee.getEmail());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.USER_CREATION_FAILED);
        }
        log.info("Finished updateEmployee method for Id" + id);
        return buildResponseModel(savedEmployee);

    }

    private RestaurantEmployee buildEditEmployeeModel(RestaurantEmployeeEditModel restaurantEmployeeRequestModel, UserAccount userAccount, RestaurantEmployee restaurantEmployee) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantEmployee.getRestaurant().getId());
        if (!restaurant.isPresent()) {
            log.error("Invalid restaurantId entered: " + restaurantEmployee.getRestaurant().getId());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_RESTAURANT_ID);
        }
        return RestaurantEmployee.builder().name(restaurantEmployeeRequestModel.getName())
                .employeeId(StringUtils.EMPTY)
                .email(restaurantEmployee.getEmail())
                .phoneNo(restaurantEmployeeRequestModel.getPhoneNo())
                .deviceId(restaurantEmployeeRequestModel.getDeviceId())
                .deviceName(restaurantEmployeeRequestModel.getDeviceName())
                .gender(restaurantEmployeeRequestModel.getGender())
                .restaurant(restaurant.get())
                .age(restaurantEmployeeRequestModel.getAge())
                .status(restaurantEmployee.getStatus())
                .user(userAccount)
                .bloodGroup(restaurantEmployeeRequestModel.getBloodGroup())
                .build();
    }

    @Override
    public void setEmployeeStatus(int id, String status) {
        log.info("Entered setEmployeeStatus method for Id" + id);
        RestaurantEmployee fetchedRestaurantEmployee = restaurantEmployeeRepository.findEmployeeById(id);
        if (Objects.isNull(fetchedRestaurantEmployee)) {
            log.error("No Records Present in restaurant_employee table for : " + id);
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_EMPLOYEE_ID);
        }
        checkAuthority(fetchedRestaurantEmployee.getRestaurant().getId());
        fetchedRestaurantEmployee.setStatus(EmployeeStatusEnum.getValue(status));
        userDetailsService.changeEmployeeUserStatus(fetchedRestaurantEmployee.getEmail(), status);
        RestaurantEmployee savedEmployee = restaurantEmployeeRepository.save(fetchedRestaurantEmployee);
        if (Objects.isNull(savedEmployee)) {
            log.error("setEmployeeStatus method failed for Id: " + id);
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.EMPLOYEE_CREATION_FAILED);
        }
        log.info("Finished setEmployeeStatus method for Id" + id);
    }

    @Override
    public void deleteEmployeeStatus(int id) {
        log.info("Entered deleteEmployeeStatus method for Id" + id);
        RestaurantEmployee fetchedRestaurantEmployee = restaurantEmployeeRepository.findEmployeeById(id);
        if (Objects.isNull(fetchedRestaurantEmployee)) {
            log.error("No Records Present in restaurant_employee table for : " + id);
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_EMPLOYEE_ID);
        }
        checkAuthority(fetchedRestaurantEmployee.getRestaurant().getId());
        UserAccount userAccount = userRepository.findUserByEmailId(fetchedRestaurantEmployee.getEmail());
        if (Objects.isNull(userAccount)) {
            log.error("No Records Present in user_account table for Email : " + fetchedRestaurantEmployee.getEmail());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_EMPLOYEE_ID);
        }
        userAccount.setIsAccountNonLocked(false);
        userRepository.save(userAccount);
        fetchedRestaurantEmployee.setDeleteFlag(1);
        restaurantEmployeeRepository.save(fetchedRestaurantEmployee);
        log.info("Finished deleteEmployeeStatus method for Id" + id);
    }

    @Override
    public RestaurantEmployeeResponseModel getEmployeeById(int id) {
        log.info("Entering getEmployeeId Method for id: " + id);
        Optional<RestaurantEmployee> fetchedRestaurantEmployee = restaurantEmployeeRepository.findById(id);
        if (!fetchedRestaurantEmployee.isPresent()) {
            log.error("No Records Present in restaurant_employee table for : " + id);
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_EMPLOYEE_ID);
        }
        checkAuthority(fetchedRestaurantEmployee.get().getRestaurant().getId());
        log.info("Finished getEmployeeId Method for id: " + id);
        return buildResponseModel(fetchedRestaurantEmployee.get());
    }

    @Override
    public List<RestaurantEmployeeResponseModel> getEmployeesByRestaurantId(int restaurantId) {
        checkAuthority(restaurantId);
        return restaurantEmployeeRepository.findEmployeeByRestaurantId(restaurantId)
                .stream().map(this::buildResponseModel).collect(Collectors.toList());
    }

    private RestaurantEmployeeResponseModel buildResponseModel(RestaurantEmployee restaurantEmployee) {
        UserAccount userAccount = userRepository.findUserByEmailId(restaurantEmployee.getEmail());
        return RestaurantEmployeeResponseModel.builder()
                .id(restaurantEmployee.getId())
                .age(restaurantEmployee.getAge())
                .bloodGroup(restaurantEmployee.getBloodGroup())
                .deviceId(restaurantEmployee.getDeviceId())
                .deviceName(restaurantEmployee.getDeviceName())
                .emailId(restaurantEmployee.getEmail())
                .employeeId(restaurantEmployee.getEmployeeId())
                .gender(restaurantEmployee.getGender())
                .name(restaurantEmployee.getName())
                .phoneNo(restaurantEmployee.getPhoneNo())
                .restaurantId(restaurantEmployee.getRestaurant().getId())
                .status(EmployeeStatusEnum.getName(restaurantEmployee.getStatus()))
                .role(restaurantEmployee.getUser().getRoles().get(0).getRoleName())
                .build();
    }

    private void checkAuthority(int restaurantId) {
        UserAccount userDetails = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authorityUtil.checkAuthority(userDetails, restaurantId);
    }
}
